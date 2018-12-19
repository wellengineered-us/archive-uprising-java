/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.FailFastException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Pipeline;
import com.syncprem.uprising.pipeline.abstractions.runtime.PipelineFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class ComplexHostImpl extends SimpleHostImpl
{
	public ComplexHostImpl()
	{
	}

	private Semaphore semaphore;
	private ExecutorService threadPool;

	private Semaphore getSemaphore()
	{
		return this.semaphore;
	}

	private void setSemaphore(Semaphore semaphore)
	{
		this.semaphore = semaphore;
	}

	private ExecutorService getThreadPool()
	{
		return this.threadPool;
	}

	private void setThreadPool(ExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}

	private static void executePipelineOnceAndRelease(Semaphore semaphore, PipelineFactory pipelineFactory, Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration)
	{
		if (semaphore == null)
			throw new ArgumentNullException("semaphore");

		if (pipelineFactory == null)
			throw new ArgumentNullException("pipelineFactory");

		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		try
		{
			createExecuteThenDisposePipeline(pipelineFactory, pipelineClass, pipelineConfiguration);
		}
		catch (Exception ex)
		{
			throw new FailFastException(ex);
		}
		finally
		{
			semaphore.release();
		}
	}

	private static boolean shutdownAndAwaitTermination(ExecutorService pool, final TimeUnit timeUnit, final long timeValue)
	{
		pool.shutdown(); // Disable new tasks from being submitted
		try
		{
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(timeValue, timeUnit))
			{
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(timeValue, timeUnit))
					return false;
			}
		}
		catch (InterruptedException ie)
		{
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();

			// preserve interrupt status
			Thread.currentThread().interrupt();

			return false;
		}

		return true;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		super.create(creating);

		if (creating)
		{
			final int size = this.getConfiguration().getPipelineConfigurations().size();

			System.out.println(String.format("run_host: thread pool/semaphore permit size = %s", size));

			this.setThreadPool(Executors.newFixedThreadPool(size));
			this.setSemaphore(new Semaphore(size));
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		super.dispose(disposing);

		if (disposing)
		{
			//this.shutdownAndAwaitTermination(this.getThreadPool());
		}
	}

	@Override
	protected void executePipelineOnce(Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration) throws Exception
	{
		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		this.getThreadPool().execute(() -> executePipelineOnceAndRelease(this.getSemaphore(), this, pipelineClass, pipelineConfiguration));
	}

	@Override
	protected boolean gracefulShutdown(boolean disposing)
	{
		boolean success;

		success = super.gracefulShutdown(disposing);

		if (success)
			success = this.tryAwaitCompletion();

		// if success return false, then the process exits with work in process...oops!

		System.out.println(String.format("gracefulShutdown(complex): success = %s, disposing = %s", success, disposing));
		return success;
	}

	@Override
	protected void maybeDispatchAwait() throws Exception
	{
		System.out.println("dispatch_loop: await...");
		this.getSemaphore().acquire();
		this.getSemaphore().release();
	}

	private boolean tryAwaitCompletion()
	{
		final TimeUnit timeUnit = Utils.getValueOrDefault(this.getConfiguration().getGracefulShutdownTimeUnit(), TimeUnit.SECONDS);
		final long timeValue = Utils.getValueOrDefault(this.getConfiguration().getGracefulShutdownTimeValue(), 30L);

		boolean success = false;

		final int size = this.getConfiguration().getPipelineConfigurations().size();

		try
		{
			// decay by half approach...
			for (long i = timeValue; i > 0L; i = i / 2)
			{
				System.out.println(String.format("gracefulShutdown(simple): timeUnit = %s, timeValue = %s, i = %s", timeUnit, timeValue, i));

				success = shutdownAndAwaitTermination(this.getThreadPool(), timeUnit, timeValue);

				System.out.println(String.format("tryAwaitCompletion/shutdownAndAwaitTermination: success = %s", success));

				if (!success)
					continue;

				success = this.getSemaphore().tryAcquire(size, timeValue, timeUnit);

				System.out.println(String.format("getSemaphore/tryAcquire: success = %s (%s#)", success, size));

				if (success)
				{
					this.getSemaphore().release(size);
					break;
				}
			}

			// if success return false, then the process exits with work in process...oops!
		}
		catch (InterruptedException iex)
		{
			// preserve interrupt status
			Thread.currentThread().interrupt();

			success = false;
		}

		System.out.println(String.format("tryAwaitCompletion: success = %s", success));
		return success;
	}
}

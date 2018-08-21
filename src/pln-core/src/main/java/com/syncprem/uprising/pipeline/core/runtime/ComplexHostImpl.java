/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.FailFastException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Pipeline;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

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

	private static void executePipelineOnceAndRelease(Semaphore semaphore, Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration)
	{
		if (semaphore == null)
			throw new ArgumentNullException("semaphore");

		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		try
		{
			executePipeline(pipelineClass, pipelineConfiguration);
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

	@Override
	protected void create(boolean creating) throws Exception
	{
		int size;

		if (this.isCreated())
			return;

		super.create(creating);

		if (creating)
		{
			size = this.getConfiguration().getPipelineConfigurations().size();

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
			// do nothing
		}
	}

	@Override
	protected void executePipelineOnce(Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration) throws Exception
	{
		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		if (!this.getSemaphore().tryAcquire(0L, TimeUnit.SECONDS))
			failFastOnlyWhen(true, Utils.EMPTY_STRING);

		this.getThreadPool().execute(() -> executePipelineOnceAndRelease(this.getSemaphore(), pipelineClass, pipelineConfiguration));
	}

	@Override
	protected void gracefulShutdown(boolean disposing)
	{
		boolean success;

		super.gracefulShutdown(disposing);

		// TODO how to handle false?
		success = this.tryAwaitCompletion();

		System.out.println(String.format("tryAwaitCompletion: success = %s, disposing = %s", success, disposing));
	}

	@Override
	protected void maybeDispatchAwait() throws Exception
	{
		System.out.println("dispatch_loop: await...");
		this.getSemaphore().acquire();
		this.getSemaphore().release();
	}

	@Override
	protected void maybeDispatchIdle() throws Exception
	{
		System.out.println("dispatch_loop: idle...");
		Thread.sleep(10000);
	}

	@Override
	protected void runInternal() throws Exception
	{
		// do nothing
		super.runInternal();
	}

	@Override
	protected boolean shouldRunDispatchLoop()
	{
		// do nothing
		return super.shouldRunDispatchLoop();
	}

	private void shutdownAndAwaitTermination(ExecutorService pool)
	{
		pool.shutdown(); // Disable new tasks from being submitted
		try
		{
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS))
			{
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		}
		catch (InterruptedException ie)
		{
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	private boolean tryAwaitCompletion()
	{
		int size;
		final long timeout = 30;
		final TimeUnit timeUnit = TimeUnit.SECONDS;

		boolean success;

		size = this.getConfiguration().getPipelineConfigurations().size();

		try
		{
			this.getThreadPool().shutdown();

			success = this.getSemaphore().tryAcquire(size, timeout, timeUnit); // TODO how to handle false?

			if (!success)
				return success; // TODO how to handle false?

			this.getSemaphore().release(size);
			System.out.println(String.format("Semaphore::tryAcquire: success = %s (%s)", success, this.getSemaphore().availablePermits()));

			success = this.getThreadPool().awaitTermination(timeout, timeUnit); // TODO how to handle false?

			System.out.println(String.format("ThreadPool::awaitTermination: success = %s", success));
		}
		catch (InterruptedException iex)
		{
			success = false;
		}

		return success;
	}
}

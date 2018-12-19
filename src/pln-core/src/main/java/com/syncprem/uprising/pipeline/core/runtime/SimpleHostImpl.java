/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.pipeline.abstractions.platform.CancellationToken;
import com.syncprem.uprising.pipeline.abstractions.runtime.*;
import com.syncprem.uprising.pipeline.core.platform.AtomicCancellationTokenImpl;

import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public class SimpleHostImpl extends AbstractHost
{
	public SimpleHostImpl()
	{
	}

	protected final int ONE = 1;
	protected final int ZERO = 0;
	private final CancellationToken cancellationToken = new AtomicCancellationTokenImpl();
	private final Semaphore mainThreadSemaphore = new Semaphore(ONE);

	protected final CancellationToken getCancellationToken()
	{
		return this.cancellationToken;
	}

	protected final Semaphore getMainThreadSemaphore()
	{
		return this.mainThreadSemaphore;
	}

	protected static Pipeline createExecuteThenDisposePipeline(PipelineFactory pipelineFactory, Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration) throws Exception
	{
		Pipeline pipeline;

		if (pipelineFactory == null)
			throw new ArgumentNullException("pipelineFactory");

		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		pipeline = pipelineFactory.createPipeline(pipelineClass);

		failFastOnlyWhen(pipeline == null, "pipeline == null");

		try (pipeline)
		{
			pipeline.setConfiguration(pipelineConfiguration);
			pipeline.create();

			final ContextFactory contextFactory = pipeline;

			try (Context context = contextFactory.createContext())
			{
				pipeline.create();
				pipeline.execute(context);

				return pipeline; // return the reference but it will be disposed...
			}
		}
	}

	private Exception _haltAndCatchFire()
	{
		// this method gets called on another thread from main()...

		try
		{
			System.out.println("_haltAndCatchFire: enter");
			this.gracefulShutdown(false);
			System.out.println("_haltAndCatchFire: leave");
		}
		catch (Exception ex)
		{
			return Utils.failFastWithException(ex);
		}

		return null;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			this.onHostUnload(new FutureTask<>(this::_haltAndCatchFire));

			super.create(creating); // intentionally placed here

			this.getCancellationToken().throwIfCancellationRequested();
		}
	}

	@Override
	protected Pipeline createPipelineInternal(Class<? extends Pipeline> clazz) throws Exception
	{
		Pipeline pipeline;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		pipeline = Utils.newObjectFromClass(clazz);

		return pipeline;
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			this.gracefulShutdown(true);
		}

		super.dispose(disposing);
	}

	protected void executePipelineOnce(Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration) throws Exception
	{
		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		try
		{
			createExecuteThenDisposePipeline(this, pipelineClass, pipelineConfiguration);
		}
		finally
		{
			this.getMainThreadSemaphore().release();
		}
	}

	protected boolean gracefulShutdown(boolean disposing)
	{
		final TimeUnit timeUnit = Utils.getValueOrDefault(this.getConfiguration().getGracefulShutdownTimeUnit(), TimeUnit.SECONDS);
		final long timeValue = Utils.getValueOrDefault(this.getConfiguration().getGracefulShutdownTimeValue(), 30L);

		boolean success = false;

		failFastOnlyWhen(!this.getCancellationToken().canBeCanceled(), "!this.getCancellationToken().canBeCanceled()");

		this.getCancellationToken().cancel();

		try
		{
			// decay by half approach...
			for (long i = timeValue; i > 0L; i = i / 2)
			{
				System.out.println(String.format("gracefulShutdown(simple): timeUnit = %s, timeValue = %s, i = %s", timeUnit, timeValue, i));

				success = this.getMainThreadSemaphore().tryAcquire(ONE, i, timeUnit);

				System.out.println(String.format("getMainThreadSemaphore/tryAcquire: success = %s (%s#)", success, ONE));

				if (success)
				{
					this.getMainThreadSemaphore().release();
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

		System.out.println(String.format("gracefulShutdown: success = %s, disposing = %s", success, disposing));
		return success;
	}

	protected void maybeDispatchAfter()
	{
		System.out.println("dispatch_loop: end");
	}

	protected void maybeDispatchAwait() throws Exception
	{
		System.out.println("dispatch_loop: await...");
	}

	protected void maybeDispatchBefore()
	{
		System.out.println("dispatch_loop: begin");
	}

	protected final void maybeDispatchIdle() throws Exception
	{
		final TimeUnit timeUnit = Utils.getValueOrDefault(this.getConfiguration().getDispatchIdleTimeUnit(), TimeUnit.SECONDS);
		final long timeValue = Utils.getValueOrDefault(this.getConfiguration().getDispatchIdleTimeValue(), 10L);

		System.out.println("dispatch_loop: idle...");
		timeUnit.sleep(timeValue);
	}

	@Override
	protected void onHostUnload(FutureTask<?> futureTask)
	{
		if (futureTask == null)
			throw new ArgumentNullException("futureTask");

		Runtime.getRuntime().addShutdownHook(new Thread(futureTask));
	}

	@Override
	protected final void runInternal() throws Exception
	{
		long loopIndex = -1L;

		do
		{
			if (++loopIndex > 0L)
			{
				// only called when dispatch loop enabled, and we expect another iteration
				this.maybeDispatchIdle();
			}

			this.maybeDispatchBefore();

			for (PipelineConfiguration pipelineConfiguration : this.getConfiguration().getPipelineConfigurations())
			{
				Class<? extends Pipeline> pipelineClass;

				if (pipelineConfiguration == null)
					continue;

				if (!pipelineConfiguration.isEnabled())
					continue;

				pipelineClass = pipelineConfiguration.getPipelineClass();

				failFastOnlyWhen(pipelineClass == null, "pipelineClass == null");

				System.out.println("dispatch_loop: execute...");

				// should always be zero/unit does not matter
				final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
				final long timeValue = ZERO;

				if (!this.getMainThreadSemaphore().tryAcquire(timeValue, timeUnit))
					failFastOnlyWhen(true, Utils.EMPTY_STRING);

				this.executePipelineOnce(pipelineClass, pipelineConfiguration);
			}

			this.maybeDispatchAfter();

			this.maybeDispatchAwait();
		}
		while (this.shouldRunDispatchLoop());
	}

	protected final boolean shouldRunDispatchLoop()
	{
		return this.getConfiguration().enableDispatchLoop() &&
				!this.getCancellationToken().isCancellationRequested();
	}
}

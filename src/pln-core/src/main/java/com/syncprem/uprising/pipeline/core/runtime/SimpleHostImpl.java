/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.AbstractHost;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Pipeline;
import com.syncprem.uprising.pipeline.abstractions.runtime.platform.CancellationToken;
import com.syncprem.uprising.pipeline.core.runtime.platform.AtomicCancellationTokenImpl;

import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public class SimpleHostImpl extends AbstractHost
{
	public SimpleHostImpl()
	{
	}

	private final CancellationToken cancellationToken = new AtomicCancellationTokenImpl();
	private final Semaphore mainThreadSemaphore = new Semaphore(1);

	protected final CancellationToken getCancellationToken()
	{
		return this.cancellationToken;
	}

	protected Semaphore getMainThreadSemaphore()
	{
		return this.mainThreadSemaphore;
	}

	protected static void executePipeline(Class<? extends Pipeline> pipelineClass, PipelineConfiguration pipelineConfiguration) throws Exception
	{
		Pipeline pipeline;

		if (pipelineClass == null)
			throw new ArgumentNullException("pipelineClass");

		if (pipelineConfiguration == null)
			throw new ArgumentNullException("pipelineConfiguration");

		pipeline = Utils.newObjectFromClass(pipelineClass);

		failFastOnlyWhen(pipeline == null, "pipeline == null");

		try (pipeline)
		{
			pipeline.setConfiguration(pipelineConfiguration);
			pipeline.create();

			try (Context context = pipeline.createContext())
			{
				pipeline.create();
				pipeline.execute(context);
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

		if (!this.getMainThreadSemaphore().tryAcquire(0L, TimeUnit.SECONDS))
			failFastOnlyWhen(true, Utils.EMPTY_STRING);

		try
		{
			executePipeline(pipelineClass, pipelineConfiguration);
		}
		finally
		{
			this.getMainThreadSemaphore().release();
		}
	}

	protected void gracefulShutdown(boolean disposing)
	{
		final int size = 1;
		final long timeout = 5;
		final TimeUnit timeUnit = TimeUnit.SECONDS;
		boolean success = false;

		this.getCancellationToken().cancel();

		try
		{
			success = this.getMainThreadSemaphore().tryAcquire(size, timeout, timeUnit); // TODO how to handle false?
		}
		catch (InterruptedException iex)
		{
			// do nothing
		}
		finally
		{
			if (success)
				this.getMainThreadSemaphore().release();
		}

		System.out.println(String.format("tryAwaitCompletion: success = %s, disposing = %s", success, disposing));
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

	protected void maybeDispatchIdle() throws Exception
	{
		System.out.println("dispatch_loop: idle...");
		Thread.sleep(2500);
	}

	@Override
	protected void runInternal() throws Exception
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

				if (pipelineClass == null)
					throw new InvalidOperationException("pipelineClass");

				System.out.println("dispatch_loop: execute...");
				this.executePipelineOnce(pipelineClass, pipelineConfiguration);
			}

			this.maybeDispatchAfter();

			this.maybeDispatchAwait();
		}
		while (this.shouldRunDispatchLoop());
	}

	@Override
	protected void onHostUnload(FutureTask<?> futureTask)
	{
		if (futureTask == null)
			throw new ArgumentNullException("futureTask");

		Runtime.getRuntime().addShutdownHook(new Thread(futureTask));
	}

	protected boolean shouldRunDispatchLoop()
	{
		return this.getConfiguration().enableDispatchLoop() &&
				!this.getCancellationToken().isCancellationRequested();
	}
}

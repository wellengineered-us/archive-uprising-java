/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.platform;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.platform.CancellationToken;

import java.util.concurrent.atomic.AtomicBoolean;

public final class AtomicCancellationTokenImpl extends AbstractLifecycle<Exception, Exception> implements CancellationToken
{
	public AtomicCancellationTokenImpl()
	{
		this(new AtomicBoolean(true));
	}

	public AtomicCancellationTokenImpl(AtomicBoolean atomicBoolean)
	{
		if (atomicBoolean == null)
			throw new ArgumentNullException("atomicBoolean");

		this.atomicBoolean = atomicBoolean;
	}

	private final AtomicBoolean atomicBoolean;

	private AtomicBoolean getAtomicBoolean()
	{
		return this.atomicBoolean;
	}

	@Override
	public boolean isCancellationRequested()
	{
		return this.getAtomicBoolean() == null || !this.getAtomicBoolean().get();
	}

	@Override
	public boolean canBeCanceled()
	{
		return this.getAtomicBoolean() != null;
	}

	@Override
	public void cancel()
	{
		if (this.getAtomicBoolean() != null)
			this.getAtomicBoolean().set(false);
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			this.cancel();
		}
	}
}

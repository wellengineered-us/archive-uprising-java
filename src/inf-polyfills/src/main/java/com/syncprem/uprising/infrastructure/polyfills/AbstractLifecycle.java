/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractLifecycle<TCreationException extends Exception, TDisposalException extends Exception> implements Creatable, Disposable
{
	protected AbstractLifecycle()
	{
	}

	private boolean isCreated;
	private boolean isDisposed;

	@JsonIgnore
	@Override
	public final boolean isCreated()
	{
		return this.isCreated;
	}

	private void setCreated(boolean isCreated)
	{
		this.isCreated = isCreated;
	}

	@JsonIgnore
	@Override
	public final boolean isDisposed()
	{
		return this.isDisposed;
	}

	private void setDisposed(boolean isDisposed)
	{
		this.isDisposed = isDisposed;
	}

	@Override
	public final void /* dispose */ close() throws TDisposalException
	{
		this.dispose();
	}

	@Override
	public final void create() throws TCreationException
	{
		if (this.isCreated())
			return;

		//GC.ReRegisterForFinalize(this);
		this.create(true);
		this.maybeSetIsCreated();
	}

	protected abstract void create(boolean creating) throws TCreationException;

	@Override
	public final void dispose() throws TDisposalException
	{
		if (this.isDisposed())
			return;

		this.dispose(true);
		//GC.SuppressFinalize(this);
		this.maybeSetIsDisposed();
	}

	protected abstract void dispose(boolean disposing) throws TDisposalException;

	protected void explicitSetIsCreated()
	{
		//GC.ReRegisterForFinalize(this);
		this.setCreated(true);
	}

	protected void explicitSetIsDisposed()
	{
		this.setDisposed(true);
		//GC.SuppressFinalize(this);
	}

	protected void maybeSetIsCreated()
	{
		this.explicitSetIsCreated();
	}

	protected void maybeSetIsDisposed()
	{
		this.explicitSetIsDisposed();
	}
}

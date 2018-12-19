/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

import java.util.UUID;

public abstract class AbstractComponent extends AbstractLifecycle<Exception, Exception> implements Component
{
	protected AbstractComponent()
	{
		this(UUID.randomUUID());
	}

	protected AbstractComponent(UUID componentId)
	{
		if (componentId == null)
			throw new ArgumentNullException("componentId");

		this.componentId = componentId;
	}

	private final UUID componentId;

	@JsonIgnore
	@Override
	public final UUID getComponentId()
	{
		return this.componentId;
	}

	@JsonIgnore
	@Override
	public final boolean isAsync()
	{
		return false;
	}

	@JsonIgnore
	@Override
	public final boolean isReusable()
	{
		return false;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			// do nothing
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			// do nothing
		}
	}
}

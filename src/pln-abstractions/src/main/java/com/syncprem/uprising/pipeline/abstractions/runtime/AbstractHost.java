/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.HostConfiguration;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import java.util.concurrent.FutureTask;

public abstract class AbstractHost extends AbstractComponent implements Host
{
	protected AbstractHost()
	{
	}

	private HostConfiguration configuration;

	@Override
	public final HostConfiguration getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public final void setConfiguration(HostConfiguration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public final Pipeline createPipeline(Class<? extends Pipeline> clazz) throws SyncPremException
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		try
		{
			return this.createPipelineInternal(clazz);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Pipeline createPipelineInternal(Class<? extends Pipeline> clazz) throws Exception;

	protected abstract void onHostUnload(FutureTask<?> futureTask);

	@Override
	public final void run() throws SyncPremException
	{
		try
		{
			this.runInternal();
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract void runInternal() throws Exception;
}

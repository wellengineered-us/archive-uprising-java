/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.HostConfiguration;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

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

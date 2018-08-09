/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractPipeline extends AbstractComponent implements Pipeline
{
	protected AbstractPipeline()
	{
	}

	private PipelineConfiguration configuration;

	@Override
	public final PipelineConfiguration getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public final void setConfiguration(PipelineConfiguration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public final Context createContext()
	{
		return this.createContextInternal();
	}

	protected abstract Context createContextInternal();

	@Override
	public final long execute(Context context) throws SyncPremException
	{
		try
		{
			return this.executeInternal(context);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract long executeInternal(Context context) throws Exception;
}

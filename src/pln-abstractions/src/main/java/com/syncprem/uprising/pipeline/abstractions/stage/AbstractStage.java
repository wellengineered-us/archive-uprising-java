/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage;

import com.syncprem.uprising.pipeline.abstractions.AbstractSpecConfComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractStage<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends AbstractSpecConfComponent<TComponentSpecificConfiguration> implements Stage<TComponentSpecificConfiguration>
{
	protected AbstractStage()
	{
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		super.create(creating);
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		super.dispose(disposing);
	}

	@Override
	public final void postExecute(Context context, RecordConfiguration configuration) throws SyncPremException
	{
		try
		{
			this.assertValidConfiguration();
			this.postExecuteInternal(context, configuration);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception;

	@Override
	public final void preExecute(Context context, RecordConfiguration configuration) throws SyncPremException
	{
		try
		{
			this.assertValidConfiguration();
			this.preExecuteInternal(context, configuration);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception;
}

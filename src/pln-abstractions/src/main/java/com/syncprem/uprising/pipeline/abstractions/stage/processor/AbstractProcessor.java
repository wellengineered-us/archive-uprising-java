/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.AbstractStage;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractProcessor<TTarget extends Component, TStageSpecificConfiguration extends StageSpecificConfiguration> extends AbstractStage<TStageSpecificConfiguration> implements Processor<TTarget, TStageSpecificConfiguration>
{
	protected AbstractProcessor()
	{
	}

	@Override
	public final TTarget process(Context context, RecordConfiguration configuration, TTarget target, ProcessDelegate<TTarget> next) throws SyncPremException
	{
		TTarget newTarget;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (target == null)
			throw new ArgumentNullException("target");

		//if (next == _null)
		//throw new ArgumentNullException("next");

		try
		{
			this.assertValidConfiguration();
			newTarget = this.processInternal(context, configuration, target, next);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return newTarget;
	}

	protected abstract TTarget processInternal(Context context, RecordConfiguration configuration, TTarget target, ProcessDelegate<TTarget> next) throws Exception;
}

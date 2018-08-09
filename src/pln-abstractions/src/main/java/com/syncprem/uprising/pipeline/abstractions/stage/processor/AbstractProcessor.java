/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.AbstractStage;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractProcessor<TStageSpecificConfiguration extends StageSpecificConfiguration> extends AbstractStage<TStageSpecificConfiguration> implements Processor<TStageSpecificConfiguration>
{
	protected AbstractProcessor()
	{
	}

	@Override
	public final Channel process(Context context, RecordConfiguration configuration, Channel channel, ProcessDelegate next) throws SyncPremException
	{
		Channel newChannel;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("runtime");

		//if (next == _null)
		//throw new ArgumentNullException("next");

		try
		{
			this.assertValidConfiguration();
			newChannel = this.processInternal(context, configuration, channel, next);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return newChannel;
	}

	protected abstract Channel processInternal(Context context, RecordConfiguration configuration, Channel channel, ProcessDelegate next) throws Exception;
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.connector.destination;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.AbstractConnector;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractDestinationConnector<TStageSpecificConfiguration extends StageSpecificConfiguration> extends AbstractConnector<TStageSpecificConfiguration> implements DestinationConnector<TStageSpecificConfiguration>
{
	protected AbstractDestinationConnector()
	{
	}

	@Override
	public final void consume(Context context, RecordConfiguration configuration, Channel channel) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		try
		{
			this.assertValidConfiguration();
			this.consumeInternal(context, configuration, channel);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract void consumeInternal(Context context, RecordConfiguration configuration, Channel channel) throws Exception;
}

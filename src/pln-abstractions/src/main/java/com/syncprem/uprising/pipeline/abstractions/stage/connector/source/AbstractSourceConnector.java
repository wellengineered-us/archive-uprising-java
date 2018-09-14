/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.connector.source;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.AbstractConnector;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractSourceConnector<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends AbstractConnector<TComponentSpecificConfiguration> implements SourceConnector<TComponentSpecificConfiguration>
{
	protected AbstractSourceConnector()
	{
	}

	@Override
	public final Channel produce(Context context, RecordConfiguration configuration) throws SyncPremException
	{
		Channel channel;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		try
		{
			this.assertValidConfiguration();
			channel = this.produceInternal(context, configuration);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return channel;
	}

	protected abstract Channel produceInternal(Context context, RecordConfiguration configuration) throws Exception;
}

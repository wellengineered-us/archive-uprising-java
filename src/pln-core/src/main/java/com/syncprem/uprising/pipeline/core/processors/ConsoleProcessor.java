/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.processors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractProcessor;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.ProcessDelegate;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class ConsoleProcessor extends AbstractProcessor<Channel, StageSpecificConfiguration>
{
	public ConsoleProcessor()
	{
	}

	@Override
	protected Class<? extends StageSpecificConfiguration> getStageSpecificConfigurationClass(Object reserved)
	{
		return StageSpecificConfiguration.class;
	}

	@Override
	protected void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");
	}


	@Override
	protected Channel processInternal(Context context, RecordConfiguration configuration, Channel channel, ProcessDelegate<Channel> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		System.out.println(String.format("%s::processInternal (before next) processor", ConsoleProcessor.class.getName()));

		if (next != null)
			channel = next.invoke(context, configuration, channel);

		System.out.println(String.format("%s::processInternal (after next) processor", ConsoleProcessor.class.getName()));

		return channel;
	}
}

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
import com.syncprem.uprising.pipeline.abstractions.stage.processor.ProcessorClosure;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullProcessor extends AbstractProcessor<StageSpecificConfiguration>
{
	public NullProcessor()
	{
	}

	public static ProcessDelegate nullMiddlewareMethod(ProcessDelegate next)
	{
		ProcessDelegate retval;

		retval = ProcessorClosure.getMiddlewareChain(NullProcessor::nullProcessorMethod, next);

		return retval;
	}

	private static Channel nullProcessorMethod(Context context, RecordConfiguration configuration, Channel channel, ProcessDelegate next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		if (next == null)
			throw new ArgumentNullException("next");

		try
		{
			channel = next.invoke(context, configuration, channel);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return channel;
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
	protected Channel processInternal(Context context, RecordConfiguration configuration, Channel channel, ProcessDelegate next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		if (next == null)
			throw new ArgumentNullException("next");

		try
		{
			channel = next.invoke(context, configuration, channel);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return channel;
	}
}

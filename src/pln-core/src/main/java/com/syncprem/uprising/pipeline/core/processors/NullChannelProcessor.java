/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.processors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareClosure;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.processor.AbstractChannelProcessor;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullChannelProcessor extends AbstractChannelProcessor<ComponentSpecificConfiguration>
{
	public NullChannelProcessor()
	{
	}

	public static MiddlewareDelegate<Channel> nullStreamProcessorMethod(MiddlewareDelegate<Channel> next)
	{
		MiddlewareDelegate<Channel> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullChannelProcessor::nullStreamProcessorMethod, next);

		return retval;
	}

	private static Channel nullStreamProcessorMethod(Context context, RecordConfiguration configuration, Channel channel, MiddlewareDelegate<Channel> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("record");

		if (next != null)
			channel = next.invoke(context, configuration, channel);

		return channel;
	}

	@Override
	protected Class<? extends ComponentSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return ComponentSpecificConfiguration.class;
	}

	@Override
	protected Stream processInternal(Context context, RecordConfiguration configuration, Stream stream, MiddlewareDelegate<Stream> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (stream == null)
			throw new ArgumentNullException("stream");

		if (next != null)
			stream = next.invoke(context, configuration, stream);

		return stream;
	}
}

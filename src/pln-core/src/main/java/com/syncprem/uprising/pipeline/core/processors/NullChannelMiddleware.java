/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.processors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Tuple;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareClosure;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractChannelMiddleware;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullChannelMiddleware extends AbstractChannelMiddleware<ComponentSpecificConfiguration>
{
	public NullChannelMiddleware()
	{
	}

	public static MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> nullChannelMiddlewareMethod(MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> next)
	{
		MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullChannelMiddleware::nullChannelMiddlewareMethod, next);

		return retval;
	}

	private static Channel nullChannelMiddlewareMethod(Tuple.Tuple2<Context, RecordConfiguration> data, Channel channel, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> next) throws SyncPremException
	{
		if (data == null)
			throw new ArgumentNullException("data");

		if (channel == null)
			throw new ArgumentNullException("channel");

		if (next != null)
			channel = next.invoke(data, channel);

		return channel;
	}

	@Override
	protected Class<? extends ComponentSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return ComponentSpecificConfiguration.class;
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
	protected Channel processInternal(Tuple.Tuple2<Context, RecordConfiguration> data, Channel channel, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> next) throws SyncPremException
	{
		if (data == null)
			throw new ArgumentNullException("data");

		if (channel == null)
			throw new ArgumentNullException("channel");

		if (next != null)
			channel = next.invoke(data, channel);

		return channel;
	}
}

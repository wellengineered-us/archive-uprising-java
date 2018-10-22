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
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractChannelMiddleware;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullChannelMiddleware extends AbstractChannelMiddleware<ComponentSpecificConfiguration>
{
	public NullChannelMiddleware()
	{
	}

	public static MiddlewareDelegate<Channel> nullChannelMiddlewareMethod(MiddlewareDelegate<Channel> next)
	{
		MiddlewareDelegate<Channel> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullChannelMiddleware::nullChannelMiddlewareMethod, next);

		return retval;
	}

	private static Channel nullChannelMiddlewareMethod(Context context, RecordConfiguration configuration, Channel channel, MiddlewareDelegate<Channel> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

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
	protected Channel processInternal(Context context, RecordConfiguration configuration, Channel channel, MiddlewareDelegate<Channel> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		if (next != null)
			channel = next.invoke(context, configuration, channel);

		return channel;
	}
}

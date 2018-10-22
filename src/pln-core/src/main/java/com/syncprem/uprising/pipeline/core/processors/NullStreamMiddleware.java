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
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractStreamMiddleware;
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullStreamMiddleware extends AbstractStreamMiddleware<ComponentSpecificConfiguration>
{
	public NullStreamMiddleware()
	{
	}

	public static MiddlewareDelegate<Stream> nullStreamMiddlewareMethod(MiddlewareDelegate<Stream> next)
	{
		MiddlewareDelegate<Stream> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullStreamMiddleware::nullStreamMiddlewareMethod, next);

		return retval;
	}

	private static Stream nullStreamMiddlewareMethod(Context context, RecordConfiguration configuration, Stream stream, MiddlewareDelegate<Stream> next) throws SyncPremException
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

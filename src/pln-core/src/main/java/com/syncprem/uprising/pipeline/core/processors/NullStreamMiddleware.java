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
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractStreamMiddleware;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullStreamMiddleware extends AbstractStreamMiddleware<ComponentSpecificConfiguration>
{
	public NullStreamMiddleware()
	{
	}

	public static MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> nullStreamMiddlewareMethod(MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> next)
	{
		MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullStreamMiddleware::nullStreamMiddlewareMethod, next);

		return retval;
	}

	private static Stream nullStreamMiddlewareMethod(Tuple.Tuple2<Context, RecordConfiguration> data, Stream stream, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> next) throws SyncPremException
	{
		if (data == null)
			throw new ArgumentNullException("data");

		if (stream == null)
			throw new ArgumentNullException("stream");

		if (next != null)
			stream = next.invoke(data, stream);

		return stream;
	}

	@Override
	protected Class<? extends ComponentSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return ComponentSpecificConfiguration.class;
	}

	@Override
	protected Stream processInternal(Tuple.Tuple2<Context, RecordConfiguration> data, Stream stream, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> next) throws SyncPremException
	{
		if (data == null)
			throw new ArgumentNullException("data");

		if (stream == null)
			throw new ArgumentNullException("stream");

		if (next != null)
			stream = next.invoke(data, stream);

		return stream;
	}
}

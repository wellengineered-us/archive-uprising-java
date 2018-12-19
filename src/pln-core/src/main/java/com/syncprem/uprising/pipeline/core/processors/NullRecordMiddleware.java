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
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractRecordMiddleware;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullRecordMiddleware extends AbstractRecordMiddleware<ComponentSpecificConfiguration>
{
	public NullRecordMiddleware()
	{
	}

	public static MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> nullRecordMiddlewareMethod(MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> next)
	{
		MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullRecordMiddleware::nullRecordMiddlewareMethod, next);

		return retval;
	}

	private static Record nullRecordMiddlewareMethod(Tuple.Tuple2<Context, RecordConfiguration> data, Record record, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> next) throws SyncPremException
	{
		if (data == null)
			throw new ArgumentNullException("data");

		if (record == null)
			throw new ArgumentNullException("record");

		if (next != null)
			record = next.invoke(data, record);

		return record;
	}

	@Override
	protected Class<? extends ComponentSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return ComponentSpecificConfiguration.class;
	}

	@Override
	protected Record processInternal(Tuple.Tuple2<Context, RecordConfiguration> data, Record record, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> next) throws SyncPremException
	{
		if (data == null)
			throw new ArgumentNullException("data");

		if (record == null)
			throw new ArgumentNullException("record");

		if (next != null)
			record = next.invoke(data, record);

		return record;
	}
}

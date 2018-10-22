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
import com.syncprem.uprising.pipeline.abstractions.stage.processor.AbstractRecordMiddleware;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public class NullRecordMiddleware extends AbstractRecordMiddleware<ComponentSpecificConfiguration>
{
	public NullRecordMiddleware()
	{
	}

	public static MiddlewareDelegate<Record> nullRecordMiddlewareMethod(MiddlewareDelegate<Record> next)
	{
		MiddlewareDelegate<Record> retval;

		retval = MiddlewareClosure.getMiddlewareChain(NullRecordMiddleware::nullRecordMiddlewareMethod, next);

		return retval;
	}

	private static Record nullRecordMiddlewareMethod(Context context, RecordConfiguration configuration, Record record, MiddlewareDelegate<Record> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (record == null)
			throw new ArgumentNullException("record");

		if (next != null)
			record = next.invoke(context, configuration, record);

		return record;
	}

	@Override
	protected Class<? extends ComponentSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return ComponentSpecificConfiguration.class;
	}

	@Override
	protected Record processInternal(Context context, RecordConfiguration configuration, Record record, MiddlewareDelegate<Record> next) throws SyncPremException
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (record == null)
			throw new ArgumentNullException("record");

		if (next != null)
			record = next.invoke(context, configuration, record);

		return record;
	}
}

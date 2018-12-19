/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Tuple;
import com.syncprem.uprising.pipeline.abstractions.AbstractSpecConfComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractRecordMiddleware<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends AbstractSpecConfComponent<TComponentSpecificConfiguration> implements RecordMiddleware<TComponentSpecificConfiguration>
{
	protected AbstractRecordMiddleware()
	{
	}

	@Override
	public final Record process(Tuple.Tuple2<Context, RecordConfiguration> data, Record target, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> next) throws SyncPremException
	{
		Record newTarget;

		if (data == null)
			throw new ArgumentNullException("data");

		if (target == null)
			throw new ArgumentNullException("target");

		//if (next == _null)
		//throw new ArgumentNullException("next");

		failFastOnlyWhen(!this.isCreated(), "!this.isCreated()");
		failFastOnlyWhen(this.isDisposed(), "this.isDisposed()");

		try
		{
			newTarget = this.processInternal(data, target, next);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return newTarget;
	}

	protected abstract Record processInternal(Tuple.Tuple2<Context, RecordConfiguration> data, Record target, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> next) throws Exception;
}

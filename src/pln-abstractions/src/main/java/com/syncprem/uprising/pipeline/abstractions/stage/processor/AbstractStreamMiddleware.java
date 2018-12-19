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
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractStreamMiddleware<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends AbstractSpecConfComponent<TComponentSpecificConfiguration> implements StreamMiddleware<TComponentSpecificConfiguration>
{
	protected AbstractStreamMiddleware()
	{
	}

	@Override
	public final Stream process(Tuple.Tuple2<Context, RecordConfiguration> data, Stream target, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> next) throws SyncPremException
	{
		Stream newTarget;

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

	protected abstract Stream processInternal(Tuple.Tuple2<Context, RecordConfiguration> data, Stream target, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> next) throws Exception;
}

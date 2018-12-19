/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Tuple;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.AbstractStage;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractChannelMiddleware<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends AbstractStage<TComponentSpecificConfiguration> implements ChannelMiddleware<TComponentSpecificConfiguration>
{
	protected AbstractChannelMiddleware()
	{
	}

	@Override
	public final Channel process(Tuple.Tuple2<Context, RecordConfiguration> data, Channel target, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> next) throws SyncPremException
	{
		Channel newTarget;

		if (data == null)
			throw new ArgumentNullException("data");

		if (target == null)
			throw new ArgumentNullException("target");

		//if (next == _null)
		//throw new ArgumentNullException("next");

		failFastOnlyWhen(!this.isCreated(), "!this.isCreated()");
		failFastOnlyWhen(this.isDisposed(), "this.isDisposed()");

		this.assertValidConfiguration();
		this.preExecute(data.getValue1(), data.getValue2());

		try
		{
			newTarget = this.processInternal(data, target, next);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		this.postExecute(data.getValue1(), data.getValue2());

		return newTarget;
	}

	protected abstract Channel processInternal(Tuple.Tuple2<Context, RecordConfiguration> data, Channel target, MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> next) throws Exception;
}

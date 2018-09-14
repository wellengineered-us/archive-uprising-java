/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.AbstractSpecConfComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractStreamProcessor<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends AbstractSpecConfComponent<TComponentSpecificConfiguration> implements StreamProcessor<TComponentSpecificConfiguration>
{
	protected AbstractStreamProcessor()
	{
	}

	@Override
	public final Record process(Context context, RecordConfiguration configuration, Record target, MiddlewareDelegate<Record> next) throws SyncPremException
	{
		Record newTarget;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (target == null)
			throw new ArgumentNullException("target");

		//if (next == _null)
		//throw new ArgumentNullException("next");

		failFastOnlyWhen(!this.isCreated(), "!this.isCreated()");
		failFastOnlyWhen(this.isDisposed(), "this.isDisposed()");

		try
		{
			newTarget = this.processInternal(context, configuration, target, next);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		return newTarget;
	}

	protected abstract Record processInternal(Context context, RecordConfiguration configuration, Record target, MiddlewareDelegate<Record> next) throws Exception;
}

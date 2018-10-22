/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
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
	public final Stream process(Context context, RecordConfiguration configuration, Stream target, MiddlewareDelegate<Stream> next) throws SyncPremException
	{
		Stream newTarget;

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

	protected abstract Stream processInternal(Context context, RecordConfiguration configuration, Stream target, MiddlewareDelegate<Stream> next) throws Exception;
}

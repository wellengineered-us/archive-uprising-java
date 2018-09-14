/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.interceptor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.AbstractStage;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractInterceptor<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends AbstractStage<TComponentSpecificConfiguration> implements Interceptor<TComponentSpecificConfiguration>
{
	protected AbstractInterceptor()
	{
	}

	@Override
	public final Channel process(Context context, RecordConfiguration configuration, Channel target, MiddlewareDelegate<Channel> next) throws SyncPremException
	{
		Channel newTarget;

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

		this.assertValidConfiguration();
		this.preExecute(context, configuration);

		try
		{
			newTarget = this.processInternal(context, configuration, target, next);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}

		this.postExecute(context, configuration);

		return newTarget;
	}

	protected abstract Channel processInternal(Context context, RecordConfiguration configuration, Channel target, MiddlewareDelegate<Channel> next) throws Exception;
}

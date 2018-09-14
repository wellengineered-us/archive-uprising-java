/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public final class MiddlewareClosure<TComponent extends Component>
{
	private MiddlewareClosure(MiddlewareToNextDelegate<TComponent> processToNext, MiddlewareDelegate<TComponent> next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		this.processToNext = processToNext;
		this.next = next;
	}

	private final MiddlewareDelegate<TComponent> next;
	private final MiddlewareToNextDelegate<TComponent> processToNext;

	private MiddlewareDelegate<TComponent> getNext()
	{
		return this.next;
	}

	private MiddlewareToNextDelegate<TComponent> getProcessToNext()
	{
		return this.processToNext;
	}

	public static <TComponent extends Component> MiddlewareDelegate<TComponent> getMiddlewareChain(MiddlewareToNextDelegate<TComponent> processToNext, MiddlewareDelegate<TComponent> next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		return new MiddlewareClosure<>(processToNext, next)::transform;
	}

	private TComponent transform(Context context, RecordConfiguration configuration, TComponent target) throws SyncPremException
	{
		return this.getProcessToNext().invoke(context, configuration, target, this.getNext());
	}
}

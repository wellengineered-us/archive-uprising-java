/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public final class MiddlewareClosure<TData, TComponent extends Creatable & Disposable>
{
	private MiddlewareClosure(MiddlewareToNextDelegate<TData, TComponent> processToNext, MiddlewareDelegate<TData, TComponent> next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		this.processToNext = processToNext;
		this.next = next;
	}

	private final MiddlewareDelegate<TData, TComponent> next;
	private final MiddlewareToNextDelegate<TData, TComponent> processToNext;

	private MiddlewareDelegate<TData, TComponent> getNext()
	{
		return this.next;
	}

	private MiddlewareToNextDelegate<TData, TComponent> getProcessToNext()
	{
		return this.processToNext;
	}

	public static <TData, TComponent extends Creatable & Disposable> MiddlewareDelegate<TData, TComponent> getMiddlewareChain(MiddlewareToNextDelegate<TData, TComponent> processToNext, MiddlewareDelegate<TData, TComponent> next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		return new MiddlewareClosure<>(processToNext, next)::transform;
	}

	private TComponent transform(TData data, TComponent target) throws SyncPremException
	{
		return this.getProcessToNext().invoke(data, target, this.getNext());
	}
}

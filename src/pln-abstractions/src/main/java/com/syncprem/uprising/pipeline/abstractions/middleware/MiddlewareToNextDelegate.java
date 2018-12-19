/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface MiddlewareToNextDelegate<TData, TComponent extends Creatable & Disposable>
{
	TComponent invoke(TData data, TComponent target, MiddlewareDelegate<TData, TComponent> next) throws SyncPremException;
}





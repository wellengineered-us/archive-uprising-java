/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.configuration.ConfigurationObject;
import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface MiddlewareBuilderExtensions<TData, TComponent extends Creatable & Disposable, TConfiguration extends ConfigurationObject>
{
	MiddlewareBuilder<TData, TComponent> from(Class<? extends Middleware<TData, TComponent, TConfiguration>> middlewareClass, TConfiguration middlewareConfiguration) throws SyncPremException;

	MiddlewareBuilder<TData, TComponent> with(Middleware<TData, TComponent, TConfiguration> middleware) throws SyncPremException;
}

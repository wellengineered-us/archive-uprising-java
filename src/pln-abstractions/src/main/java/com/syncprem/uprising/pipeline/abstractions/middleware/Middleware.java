/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.configuration.Configurable;
import com.syncprem.uprising.infrastructure.configuration.ConfigurationObject;
import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Middleware<TData, TComponent extends Creatable & Disposable, TConfiguration extends ConfigurationObject> extends Component, Configurable<TConfiguration>
{
	TComponent process(TData data, TComponent target, MiddlewareDelegate<TData, TComponent> next) throws SyncPremException;
}

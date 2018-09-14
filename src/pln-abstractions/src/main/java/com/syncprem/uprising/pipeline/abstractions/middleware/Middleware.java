/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.configuration.Configurable;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Middleware<TComponent extends Component, TConfiguration extends ComponentConfiguration> extends Component, Configurable<TConfiguration>
{
	TComponent process(Context context, RecordConfiguration configuration, TComponent target, MiddlewareDelegate<TComponent> next) throws SyncPremException;
}

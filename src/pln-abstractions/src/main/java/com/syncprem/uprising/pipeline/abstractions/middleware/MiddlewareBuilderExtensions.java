/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentConfiguration;

public interface MiddlewareBuilderExtensions<TComponent extends Component, TConfiguration extends ComponentConfiguration>
{
	MiddlewareBuilder<TComponent> from(Class<? extends Middleware<TComponent, TConfiguration>> middlewareClass, TConfiguration middlewareConfiguration) throws Exception;

	MiddlewareBuilder<TComponent> with(Middleware<TComponent, TConfiguration> middleware) throws Exception;
}

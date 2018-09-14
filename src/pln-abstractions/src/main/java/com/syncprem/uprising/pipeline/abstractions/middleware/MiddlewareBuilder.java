/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.pipeline.abstractions.Component;

public interface MiddlewareBuilder<TComponent extends Component>
{
	MiddlewareDelegate<TComponent> build() throws Exception;

	MiddlewareBuilder<TComponent> use(MiddlewareChainDelegate<MiddlewareDelegate<TComponent>, MiddlewareDelegate<TComponent>> middleware) throws Exception;
}

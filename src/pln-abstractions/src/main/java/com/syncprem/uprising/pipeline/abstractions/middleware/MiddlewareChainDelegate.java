/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.streamingio.primitives.SyncPremException;

@FunctionalInterface
public interface MiddlewareChainDelegate<TInput, TOutput>
{
	TOutput invoke(TInput input) throws SyncPremException;
}





/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

@FunctionalInterface
public interface ChainDelegate<T, R>
{
	R invoke(T input);
}





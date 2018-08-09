/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

public interface ProcessorBuilder
{
	ProcessDelegate build();

	ProcessorBuilder new_();

	ProcessorBuilder use(ChainDelegate<ProcessDelegate, ProcessDelegate> middleware);
}

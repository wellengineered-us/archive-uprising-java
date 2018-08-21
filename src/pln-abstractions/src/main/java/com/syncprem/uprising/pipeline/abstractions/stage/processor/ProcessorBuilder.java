/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.pipeline.abstractions.Component;

public interface ProcessorBuilder<TTarget extends Component>
{
	ProcessDelegate<TTarget> build();

	ProcessorBuilder<TTarget> new_();

	ProcessorBuilder<TTarget> use(ChainDelegate<ProcessDelegate<TTarget>, ProcessDelegate<TTarget>> middleware);
}

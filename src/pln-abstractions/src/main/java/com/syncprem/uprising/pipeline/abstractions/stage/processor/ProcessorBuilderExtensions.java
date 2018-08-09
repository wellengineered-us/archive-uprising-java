/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedStageConfiguration;

public interface ProcessorBuilderExtensions
{
	ProcessorBuilder from(Processor<? extends StageSpecificConfiguration> processor, UntypedStageConfiguration stageConfiguration) throws Exception;

	ProcessorBuilder from(Class<? extends Processor<? extends StageSpecificConfiguration>> processorClass, UntypedStageConfiguration stageConfiguration) throws Exception;
}

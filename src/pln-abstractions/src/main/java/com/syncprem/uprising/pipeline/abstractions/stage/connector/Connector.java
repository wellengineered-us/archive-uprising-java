/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.connector;

import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.stage.Stage;

public interface Connector<TStageSpecificConfiguration extends StageSpecificConfiguration> extends Stage<TStageSpecificConfiguration>
{
	String CONTEXT_COMPONENT_SCOPED_SCHEMA = "context_component_scoped_schema";
}

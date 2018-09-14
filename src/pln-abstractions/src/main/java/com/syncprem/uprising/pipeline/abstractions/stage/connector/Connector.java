/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.connector;

import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.stage.Stage;

public interface Connector<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends Stage<TComponentSpecificConfiguration>
{
	String CONTEXT_COMPONENT_SCOPED_SCHEMA = "context_component_scoped_schema";
}

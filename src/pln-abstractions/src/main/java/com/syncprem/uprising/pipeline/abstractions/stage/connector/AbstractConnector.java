/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.connector;

import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.stage.AbstractStage;

public abstract class AbstractConnector<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends AbstractStage<TComponentSpecificConfiguration> implements Connector<TComponentSpecificConfiguration>
{
	protected AbstractConnector()
	{
	}
}

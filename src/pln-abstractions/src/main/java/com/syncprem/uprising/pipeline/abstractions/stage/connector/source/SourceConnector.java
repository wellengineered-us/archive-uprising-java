/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.connector.source;

import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.Connector;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface SourceConnector<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends Connector<TComponentSpecificConfiguration>
{
	Channel produce(Context context, RecordConfiguration configuration) throws SyncPremException;
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage;

import com.syncprem.uprising.infrastructure.configuration.Configurable;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.Specifiable;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Stage<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends Component, Configurable<UntypedComponentConfiguration>, Specifiable<TComponentSpecificConfiguration>
{
	void postExecute(Context context, RecordConfiguration configuration) throws SyncPremException;

	void preExecute(Context context, RecordConfiguration configuration) throws SyncPremException;
}

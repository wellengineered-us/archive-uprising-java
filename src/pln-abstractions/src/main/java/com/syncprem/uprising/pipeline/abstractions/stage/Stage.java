/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage;

import com.syncprem.uprising.infrastructure.configuration.Configurable;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.Specifiable;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedStageConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Stage<TStageSpecificConfiguration extends StageSpecificConfiguration>
		extends Component, Configurable<UntypedStageConfiguration>, Specifiable<TStageSpecificConfiguration>
{
	void postExecute(Context context, RecordConfiguration configuration) throws SyncPremException;

	void preExecute(Context context, RecordConfiguration configuration) throws SyncPremException;
}

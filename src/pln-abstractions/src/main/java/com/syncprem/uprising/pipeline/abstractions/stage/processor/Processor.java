/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.Stage;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Processor<TTarget extends Component, TStageSpecificConfiguration extends StageSpecificConfiguration> extends Stage<TStageSpecificConfiguration>
{
	TTarget process(Context context, RecordConfiguration configuration, TTarget target, ProcessDelegate<TTarget> next) throws SyncPremException;
}

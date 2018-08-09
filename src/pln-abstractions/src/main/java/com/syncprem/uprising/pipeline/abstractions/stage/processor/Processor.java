/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.Stage;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Processor<TStageSpecificConfiguration extends StageSpecificConfiguration> extends Stage<TStageSpecificConfiguration>
{
	Channel process(Context context, RecordConfiguration configuration, Channel channel, ProcessDelegate next) throws SyncPremException;
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

@FunctionalInterface
public interface ProcessDelegate<TTarget extends Component>
{
	TTarget invoke(Context context, RecordConfiguration configuration, TTarget target) throws SyncPremException;
}





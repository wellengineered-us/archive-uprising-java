/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage;

import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

@FunctionalInterface
public interface ExecuteDelegate
{
	void invoke(Context context, RecordConfiguration configuration) throws SyncPremException;
}
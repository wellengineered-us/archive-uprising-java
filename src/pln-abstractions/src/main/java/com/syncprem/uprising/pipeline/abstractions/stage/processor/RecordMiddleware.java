/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.Middleware;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;

public interface RecordMiddleware<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends Middleware<Record, UntypedComponentConfiguration>
{
}

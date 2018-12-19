/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.Tuple;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.Middleware;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;

public interface RecordMiddleware<TComponentSpecificConfiguration extends ComponentSpecificConfiguration>
		extends Middleware<Tuple.Tuple2<Context, RecordConfiguration>, Record, UntypedComponentConfiguration>
{
}

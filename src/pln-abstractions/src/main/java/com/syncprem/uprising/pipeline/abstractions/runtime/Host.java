/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.configuration.Configurable;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.HostConfiguration;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface Host extends Component, Configurable<HostConfiguration>, PipelineFactory
{
	void run() throws SyncPremException;
}

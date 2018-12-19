/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface PipelineFactory
{
	Pipeline createPipeline(Class<? extends Pipeline> clazz) throws SyncPremException;
}

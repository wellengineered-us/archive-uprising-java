/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.streamingio.primitives.*;

public interface RecordFactory
{
	Record createEmptyRecord() throws SyncPremException;

	Record createRecord(Schema schema, Payload payload, String topic, Partition partition, Offset offset) throws SyncPremException;
}

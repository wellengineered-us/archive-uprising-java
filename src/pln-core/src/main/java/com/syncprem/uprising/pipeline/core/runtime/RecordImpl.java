/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.runtime.AbstractRecord;
import com.syncprem.uprising.streamingio.primitives.*;

public final class RecordImpl extends AbstractRecord
{
	public RecordImpl(Schema schema, Payload payload, String topic, Partition partition, Offset offset)
	{
		super(schema, payload, topic, partition, offset);
	}

	public static final RecordImpl EMPTY = new RecordImpl(SchemaBuilderImpl.EMPTY, PayloadImpl.EMPTY, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE);
}

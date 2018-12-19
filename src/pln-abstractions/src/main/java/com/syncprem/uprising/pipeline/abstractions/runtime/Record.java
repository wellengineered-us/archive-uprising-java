/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.streamingio.primitives.Offset;
import com.syncprem.uprising.streamingio.primitives.Partition;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.primitives.Schema;

import java.time.Instant;

public interface Record extends Component
{
	long getIndex();

	void setIndex(long index);

	Instant getInstant();

	Offset getOffset();

	Partition getPartition();

	Payload getPayload();

	Schema getSchema();

	String getTopic();
}

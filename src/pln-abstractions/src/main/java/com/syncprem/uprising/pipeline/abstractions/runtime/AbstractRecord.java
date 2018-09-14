/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.streamingio.primitives.Offset;
import com.syncprem.uprising.streamingio.primitives.Partition;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.primitives.Schema;

import java.time.Instant;

public abstract class AbstractRecord extends AbstractComponent implements Record
{
	protected AbstractRecord(Schema schema, Payload payload, String topic, Partition partition, Offset offset)
	{
		if (schema == null)
			throw new ArgumentNullException("schema");

		if (payload == null)
			throw new ArgumentNullException("record");

		if (topic == null)
			throw new ArgumentNullException("topic");

		/*if (partition == null)
			throw new ArgumentNullException("partition");

		if (offset == null)
			throw new ArgumentNullException("offset");

		if (topic.isEmpty())
			throw new ArgumentOutOfRangeException("topic");*/

		this.schema = schema;
		this.payload = payload;
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
		this.instant = Instant.now();
	}

	private final Instant instant;
	private final Offset offset;
	private final Partition partition;
	private final Payload payload;
	private final Schema schema;
	private final String topic;
	private long index;

	@Override
	public long getIndex()
	{
		return this.index;
	}

	@Override
	public void setIndex(long index)
	{
		this.index = index;
	}

	@Override
	public Instant getInstant()
	{
		return this.instant;
	}

	@Override
	public Offset getOffset()
	{
		return this.offset;
	}

	@Override
	public Partition getPartition()
	{
		return this.partition;
	}

	@Override
	public Payload getPayload()
	{
		return this.payload;
	}

	@Override
	public Schema getSchema()
	{
		return this.schema;
	}

	@Override
	public String getTopic()
	{
		return this.topic;
	}

	@Override
	public String toString()
	{
		return Utils.toStringSafe(this.getPayload());
	}
}

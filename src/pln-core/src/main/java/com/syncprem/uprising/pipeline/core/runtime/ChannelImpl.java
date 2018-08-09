/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;

public final class ChannelImpl extends AbstractComponent implements Channel
{
	public ChannelImpl(LifecycleIterator<Record> records)
	{
		if (records == null)
			throw new ArgumentNullException("records");

		this.records = new StreamImpl(records);
	}

	private final Stream records;

	@Override
	public Stream getRecords()
	{
		return this.records;
	}
}

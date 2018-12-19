/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;

public abstract class AbstractStream extends AbstractComponent implements Stream
{
	protected AbstractStream(LifecycleIterator<Record> records)
	{
		if (records == null)
			throw new ArgumentNullException("records");

		this.records = records;
	}

	private final LifecycleIterator<Record> records;

	private LifecycleIterator<Record> getRecords()
	{
		return records;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			this.getRecords().create();
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			this.getRecords().dispose();
		}
	}

	@Override
	public boolean hasNext()
	{
		return this.getRecords().hasNext();
	}

	@Override
	public Record next()
	{
		return this.getRecords().next();
	}
}

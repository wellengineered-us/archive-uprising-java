/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;

public abstract class AbstractChannel extends AbstractComponent implements Channel
{
	protected AbstractChannel(Stream stream)
	{
		if (stream == null)
			throw new ArgumentNullException("stream");

		this.stream = stream;
	}

	private final Stream stream;

	@Override
	public Stream getRecords()
	{
		return this.stream;
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
}

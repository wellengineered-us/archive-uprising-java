/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;

public final class JdbcStreamingResultImpl implements JdbcStreamingResult
{
	public JdbcStreamingResultImpl()
	{
	}

	private LifecycleIterator<JdbcStreamingRecord> records;
	private int recordsAffected;
	private long resultIndex;

	@Override
	public LifecycleIterator<JdbcStreamingRecord> getRecords()
	{
		return this.records;
	}

	public void setRecords(LifecycleIterator<JdbcStreamingRecord> records)
	{
		this.records = records;
	}

	@Override
	public int getRecordsAffected()
	{
		return this.recordsAffected;
	}

	public void setRecordsAffected(int recordsAffected)
	{
		this.recordsAffected = recordsAffected;
	}

	@Override
	public long getResultIndex()
	{
		return this.resultIndex;
	}

	public void setResultIndex(long resultIndex)
	{
		this.resultIndex = resultIndex;
	}
}

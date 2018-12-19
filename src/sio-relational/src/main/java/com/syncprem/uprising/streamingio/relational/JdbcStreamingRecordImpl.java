/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import java.util.LinkedHashMap;

public final class JdbcStreamingRecordImpl extends LinkedHashMap<String, Object> implements JdbcStreamingRecord
{
	public JdbcStreamingRecordImpl()
	{
	}

	private static final long serialVersionUID = -5365630128856068164L;
	private long recordIndex;
	private long resultIndex;

	@Override
	public long getRecordIndex()
	{
		return this.recordIndex;
	}

	public void setRecordIndex(long recordIndex)
	{
		this.recordIndex = recordIndex;
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

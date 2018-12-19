/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.*;

import java.util.Iterator;

public abstract class AbstractJdbcStreamingDataReader extends AbstractLifecycle<Exception, Exception> implements JdbcStreamingDataReader
{
	protected AbstractJdbcStreamingDataReader()
	{
	}

	protected static final long DEFAULT_INDEX = -1;
	protected static final int DEFAULT_INDEX_INT = -1;
	protected static final int DEFAULT_RECORDS_AFFECTED = -1;
	private String[] columnNamesByIndexZeroBased;
	private long recordIndex = -1;
	private long resultIndex = -1;

	protected final String[] getColumnNamesByIndexZeroBased()
	{
		return this.columnNamesByIndexZeroBased;
	}

	protected final void setColumnNamesByIndexZeroBased(String[] columnNamesByIndexZeroBased)
	{
		this.columnNamesByIndexZeroBased = columnNamesByIndexZeroBased;
	}

	@Override
	public final int getFieldCount() throws Exception
	{
		final String[] columnNames = this.getColumnNamesByIndexZeroBased();

		if (columnNames == null)
			return DEFAULT_INDEX_INT;

		return columnNames.length;
	}

	@Override
	public final long getRecordIndex()
	{
		return this.recordIndex;
	}

	protected final void setRecordIndex(long recordIndex)
	{
		this.recordIndex = recordIndex;
	}

	@Override
	public int getRecordsAffected() throws Exception
	{
		return DEFAULT_RECORDS_AFFECTED;
	}

	@Override
	public final long getResultIndex()
	{
		return this.resultIndex;
	}

	protected final void setResultIndex(long resultIndex)
	{
		this.resultIndex = resultIndex;
	}

	@Override
	public final JdbcStreamingDataReader getData(int columnIndexZeroBased) throws Exception
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final JdbcStreamingDataReader getData(String columnName) throws Exception
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final String getDataTypeName(int columnIndexZeroBased)
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final String getDataTypeName(String columnName)
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final Class<?> getFieldType(int columnIndexZeroBased)
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final Class<?> getFieldType(String columnName)
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final String getName(int columnIndexZeroBased) throws Exception
	{
		final String[] columnNames = this.getColumnNamesByIndexZeroBased();

		if (columnNames == null)
			throw new IndexOutOfRangeException("columnIndexZeroBased");

		if (columnIndexZeroBased >= columnNames.length)
			throw new IndexOutOfRangeException("columnIndexZeroBased");

		final String columnName = columnNames[columnIndexZeroBased];

		return columnName;
	}

	@Override
	public final int getOrdinal(String columnName) throws Exception
	{
		if (columnName == null)
			throw new ArgumentNullException("columnName");

		final String[] columnNames = this.getColumnNamesByIndexZeroBased();

		if (columnNames == null)
			throw new IndexOutOfRangeException("columnName");

		for (int i = 0; i < columnNames.length; i++)
		{
			final String columnName_ = Utils.getValueOrDefault(columnNames[i], Utils.EMPTY_STRING);

			if (columnName.equalsIgnoreCase(columnName_))
				return i;
		}

		throw new IndexOutOfRangeException("columnName");
	}

	@Override
	public final Object getValue(int columnIndexZeroBased) throws Exception
	{
		final String columnName = this.getName(columnIndexZeroBased);

		if (columnName == null)
			throw new IndexOutOfRangeException("columnIndexZeroBased");

		final Object value = this.getValue(columnName);

		return value;
	}

	@Override
	public final Iterator<Object> getValues(int columnIndexZeroBased) throws Exception
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final Iterator<Object> getValues(String columnName) throws Exception
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	protected abstract boolean hasNextRecord() throws Exception;

	protected abstract boolean hasNextResult() throws Exception;

	protected final void incrementRecordIndex()
	{
		this.setRecordIndex(this.getRecordIndex() + 1); // increment
	}

	protected final void incrementResultIndex()
	{
		this.setResultIndex(this.getResultIndex() + 1); // increment
	}

	@Override
	public final boolean isDbNull(int columnIndexZeroBased)
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}

	@Override
	public final boolean isDbNull(String columnName)
	{
		throw new NotImplementedException(Utils.formatCallerInfo());
	}
}

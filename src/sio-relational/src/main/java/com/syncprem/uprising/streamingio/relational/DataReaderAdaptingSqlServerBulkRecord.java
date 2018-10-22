/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.FailFastException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class DataReaderAdaptingSqlServerBulkRecord extends AbstractLifecycle<Exception, Exception> implements ISQLServerBulkRecord
{
	public DataReaderAdaptingSqlServerBulkRecord(JdbcStreamingDataReader dataReader, Iterable<JdbcStreamingColumn> columns)
	{
		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		if (columns == null)
			throw new ArgumentNullException("columns");

		this.dataReader = dataReader;
		this.columns = columns;
	}

	final private Iterable<JdbcStreamingColumn> columns;
	final private JdbcStreamingDataReader dataReader;

	@Override
	public final Set<Integer> getColumnOrdinals()
	{
		try
		{
			Set<Integer> columnOrdinals;
			final Iterable<JdbcStreamingColumn> columns = this.getColumns();

			failFastOnlyWhen(columns == null, "columns == null");

			columnOrdinals = new HashSet<>();

			if (columns != null)
			{
				int columnIndex = -1;
				for (JdbcStreamingColumn column : columns)
				{
					if (column == null)
						continue;

					final int metaIndex = ++columnIndex + 1; // offset to JDBC one-based goofiness

					if (column.getColumnOrdinal() == null ||
							column.getColumnOrdinal().intValue() == metaIndex)
						columnOrdinals.add(metaIndex);
				}
			}

			return columnOrdinals;
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on getColumnOrdinals(): ", ex);
		}
	}

	private Iterable<JdbcStreamingColumn> getColumns()
	{
		return this.columns;
	}

	private JdbcStreamingDataReader getDataReader()
	{
		return this.dataReader;
	}

	@Override
	public final Object[] getRowData() throws SQLServerException
	{
		try
		{
			final Iterable<JdbcStreamingColumn> columns = this.getColumns();

			failFastOnlyWhen(columns == null, "columns == null");
			List<Object> data;

			data = new ArrayList<>();

			if (columns != null)
			{
				int columnIndex = -1;
				for (JdbcStreamingColumn column : columns)
				{
					if (column == null)
						continue;

					final int metaIndex = ++columnIndex + 1; // offset to JDBC one-based goofiness

					if (column.getColumnOrdinal() == null ||
							column.getColumnOrdinal().intValue() == metaIndex)
					{
						final Object value = this.getDataReader().getValue(columnIndex);
						data.add(value);
					}
				}
			}

			return data.toArray();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on getRowData(): ", ex);
		}
	}

	@Override
	protected void create(boolean creating) throws Exception
	{

	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{

	}

	private JdbcStreamingColumn findColumnByOrdinal(int columnOrdinalOneBasedPerJdbc) throws Exception
	{
		final Iterable<JdbcStreamingColumn> columns = this.getColumns();

		failFastOnlyWhen(columns == null, "columns == null");

		if (columns != null)
		{
			int columnIndex = -1;
			for (JdbcStreamingColumn column : columns)
			{
				if (column == null)
					continue;

				final int metaIndex = ++columnIndex + 1; // offset to JDBC one-based goofiness

				if ((column.getColumnOrdinal() == null ||
						column.getColumnOrdinal().intValue() == metaIndex) &&
						metaIndex == columnOrdinalOneBasedPerJdbc)
					return column;
			}
		}

		return null;
	}

	@Override
	public final String getColumnName(int i)
	{
		try
		{
			final JdbcStreamingColumn column = this.findColumnByOrdinal(i);

			failFastOnlyWhen(column == null, "column == null");

			return column.getColumnName();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on getColumnName(): ", ex);
		}
	}

	@Override
	public final int getColumnType(int i)
	{
		try
		{
			final JdbcStreamingColumn column = this.findColumnByOrdinal(i);

			failFastOnlyWhen(column == null, "column == null");

			return column.getJdbcType().getVendorTypeNumber();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on getColumnType(): ", ex);
		}
	}

	@Override
	public final int getPrecision(int i)
	{
		try
		{
			final JdbcStreamingColumn column = this.findColumnByOrdinal(i);

			failFastOnlyWhen(column == null, "column == null");

			return column.getPrecision();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on getPrecision(): ", ex);
		}
	}

	@Override
	public final int getScale(int i)
	{
		try
		{
			final JdbcStreamingColumn column = this.findColumnByOrdinal(i);

			failFastOnlyWhen(column == null, "column == null");

			return column.getScale();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on getScale(): ", ex);
		}
	}

	@Override
	public final boolean isAutoIncrement(int i)
	{
		try
		{
			final JdbcStreamingColumn column = this.findColumnByOrdinal(i);

			failFastOnlyWhen(column == null, "column == null");

			return column.isAutoIncrement();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on isAutoIncrement(): ", ex);
		}
	}

	@Override
	public final boolean next() throws SQLServerException
	{
		try
		{
			return this.getDataReader().readRecord();
		}
		catch (Exception ex)
		{
			throw new FailFastException("Error on next(): ", ex);
		}
	}

	@Override
	public void addColumnMetadata(int i, String s, int i1, int i2, int i3, DateTimeFormatter dateTimeFormatter) throws SQLServerException
	{

	}

	@Override
	public void addColumnMetadata(int i, String s, int i1, int i2, int i3) throws SQLServerException
	{

	}

	@Override
	public void setTimestampWithTimezoneFormat(String s)
	{

	}

	@Override
	public void setTimestampWithTimezoneFormat(DateTimeFormatter dateTimeFormatter)
	{

	}

	@Override
	public void setTimeWithTimezoneFormat(String s)
	{

	}

	@Override
	public void setTimeWithTimezoneFormat(DateTimeFormatter dateTimeFormatter)
	{

	}

	@Override
	public DateTimeFormatter getColumnDateTimeFormatter(int i)
	{
		return null;
	}
}

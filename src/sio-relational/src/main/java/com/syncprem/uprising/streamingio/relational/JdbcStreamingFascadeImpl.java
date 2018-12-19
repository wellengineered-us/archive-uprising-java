/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.*;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.Savepoint;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.*;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class JdbcStreamingFascadeImpl implements JdbcStreamingFascade
{
	public JdbcStreamingFascadeImpl()
	{
	}

	@Override
	public JdbcStreamingColumn createColumn(String sourceColumn, JDBCType columnType, int columnSize, byte columnPrecision, byte columnScale, boolean columnNullable, String columnName) throws Exception
	{
		UUID __ = __enter();
		JdbcStreamingColumnImpl jdbcStreamingColumn;

		jdbcStreamingColumn = new JdbcStreamingColumnImpl();

		jdbcStreamingColumn.setColumnName(columnName);
		jdbcStreamingColumn.setColumnSize(columnSize);
		jdbcStreamingColumn.setJdbcType(columnType);
		jdbcStreamingColumn.setNullable(columnNullable);
		jdbcStreamingColumn.setPrecision((int) columnPrecision);
		jdbcStreamingColumn.setScale((int) columnScale);
		jdbcStreamingColumn.setSourceColumn(sourceColumn);

		//__watching(__, jdbcStreamingColumn);
		__leave(__);

		return jdbcStreamingColumn;
	}

	@Override
	public JdbcStreamingParameter createParameter(String sourceColumn, ParameterDirection parameterDirection, JDBCType parameterType, int parameterSize, byte parameterPrecision, byte parameterScale, boolean parameterIsNullable, String parameterName, Object parameterValue) throws Exception
	{
		UUID __ = __enter();
		JdbcStreamingParameterImpl jdbcStreamingParameter;

		jdbcStreamingParameter = new JdbcStreamingParameterImpl();

		jdbcStreamingParameter.setParameterName(parameterName);
		jdbcStreamingParameter.setSize(parameterSize);
		jdbcStreamingParameter.setValue(parameterValue);
		jdbcStreamingParameter.setDirection(parameterDirection);
		jdbcStreamingParameter.setJdbcType(parameterType);
		jdbcStreamingParameter.setNullable(parameterIsNullable);
		jdbcStreamingParameter.setPrecision(parameterPrecision);
		jdbcStreamingParameter.setScale(parameterScale);
		jdbcStreamingParameter.setSourceColumn(sourceColumn);

		//__watching(__, jdbcStreamingParameter);
		__leave(__);

		return jdbcStreamingParameter;
	}

	@Override
	public JdbcStreamingDataReader executeReader(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, CommandBehavior commandBehavior, Integer commandTimeout, boolean commandPrepare) throws Exception
	{
		final UUID __ = __enter();
		JdbcStreamingDataReader dataReader;

		if (connection == null)
			throw new ArgumentNullException("connection");

		if (commandType == null)
			throw new ArgumentNullException("commandType");

		if (commandText == null)
			throw new ArgumentNullException("commandText");

		if (commandParameters == null)
			throw new ArgumentNullException("commandParameters");

		if (commandBehavior == null)
			throw new ArgumentNullException("commandBehavior");

		dataReader = JdbcStreamingDataReaderImpl.create(connection, commandType, commandText, commandParameters, commandBehavior, commandTimeout, commandPrepare);
		__watching(__, dataReader);

		__leave(__);

		return dataReader;
	}

	@Override
	public LifecycleIterator<JdbcStreamingRecord> executeRecords(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, RecordsAffectedCallback recordsAffectedCallback) throws Exception
	{
		final UUID __ = __enter();
		final boolean COMMAND_PREPARE = true;
		final Integer COMMAND_TIMEOUT = null;
		final CommandBehavior COMMAND_BEHAVIOR = CommandBehavior.DEFAULT;

		JdbcStreamingDataReader dataReader;
		LifecycleIterator<JdbcStreamingRecord> records;

		if (connection == null)
			throw new ArgumentNullException("connection");

		if (commandType == null)
			throw new ArgumentNullException("commandType");

		if (commandText == null)
			throw new ArgumentNullException("commandText");

		if (commandParameters == null)
			throw new ArgumentNullException("commandParameters");

		dataReader = this.executeReader(connection, savepoint, commandType, commandText, commandParameters, COMMAND_BEHAVIOR, COMMAND_TIMEOUT, COMMAND_PREPARE);
		__watching(__, dataReader);

		records = this.getRecordsFromDataReader(dataReader, recordsAffectedCallback);
		failFastOnlyWhen(records == null, "records == null");
		__watching(__, records);

		records = new AbstractForEachYieldIterator<JdbcStreamingRecord, JdbcStreamingRecord>(records, (index, item) -> item)
		{
			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				if (dataReader != null)
				{
					dataReader.dispose();
					__dispose(__, dataReader);
				}

				super.dispose(disposing);
			}
		};

		__watching(__, records);
		__leave(__);

		return records;
	}

	@Override
	public LifecycleIterator<JdbcStreamingResult> executeResults(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters) throws Exception
	{
		final UUID __ = __enter();
		final boolean COMMAND_PREPARE = true;
		final Integer COMMAND_TIMEOUT = null;
		final CommandBehavior COMMAND_BEHAVIOR = CommandBehavior.DEFAULT;

		JdbcStreamingDataReader dataReader;
		LifecycleIterator<JdbcStreamingResult> results;

		if (connection == null)
			throw new ArgumentNullException("connection");

		if (commandType == null)
			throw new ArgumentNullException("commandType");

		if (commandText == null)
			throw new ArgumentNullException("commandText");

		if (commandParameters == null)
			throw new ArgumentNullException("commandParameters");

		dataReader = this.executeReader(connection, savepoint, commandType, commandText, commandParameters, COMMAND_BEHAVIOR, COMMAND_TIMEOUT, COMMAND_PREPARE);
		failFastOnlyWhen(dataReader == null, "dataReader == null");
		__watching(__, dataReader);

		results = this.getResultsFromDataReader(dataReader);
		failFastOnlyWhen(results == null, "results == null");
		__watching(__, results);

		results = new AbstractForEachYieldIterator<JdbcStreamingResult, JdbcStreamingResult>(results, (index, item) -> item)
		{
			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				if (dataReader != null)
				{
					dataReader.dispose();
					__dispose(__, dataReader);
				}

				super.dispose(disposing);
			}
		};

		__watching(__, results);
		__leave(__);

		return results;
	}

	@Override
	public LifecycleIterator<JdbcStreamingRecord> executeSchemaRecords(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, RecordsAffectedCallback recordsAffectedCallback) throws Exception
	{
		final UUID __ = __enter();
		final boolean COMMAND_PREPARE = true;
		final Integer COMMAND_TIMEOUT = null;
		final CommandBehavior COMMAND_BEHAVIOR = CommandBehavior.SCHEMA_ONLY;

		JdbcStreamingDataReader dataReader;
		LifecycleIterator<JdbcStreamingRecord> records;

		if (connection == null)
			throw new ArgumentNullException("connection");

		if (commandType == null)
			throw new ArgumentNullException("commandType");

		if (commandText == null)
			throw new ArgumentNullException("commandText");

		if (commandParameters == null)
			throw new ArgumentNullException("commandParameters");

		dataReader = this.executeReader(connection, savepoint, commandType, commandText, commandParameters, COMMAND_BEHAVIOR, COMMAND_TIMEOUT, COMMAND_PREPARE);
		failFastOnlyWhen(dataReader == null, "dataReader == null");
		__watching(__, dataReader);

		records = this.getSchemaRecordsFromDataReader(dataReader, recordsAffectedCallback);
		failFastOnlyWhen(records == null, "records == null");
		__watching(__, records);

		records = new AbstractForEachYieldIterator<JdbcStreamingRecord, JdbcStreamingRecord>(records, (index, item) -> item)
		{
			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				if (dataReader != null)
				{
					dataReader.dispose();
				}

				super.dispose(disposing);
			}
		};

		__watching(__, records);
		__leave(__);

		return records;
	}

	@Override
	public LifecycleIterator<JdbcStreamingResult> executeSchemaResults(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters) throws Exception
	{
		final UUID __ = __enter();
		final boolean COMMAND_PREPARE = true;
		final Integer COMMAND_TIMEOUT = null;
		final CommandBehavior COMMAND_BEHAVIOR = CommandBehavior.SCHEMA_ONLY;

		JdbcStreamingDataReader dataReader;
		LifecycleIterator<JdbcStreamingResult> results;

		if (connection == null)
			throw new ArgumentNullException("connection");

		if (commandType == null)
			throw new ArgumentNullException("commandType");

		if (commandText == null)
			throw new ArgumentNullException("commandText");

		if (commandParameters == null)
			throw new ArgumentNullException("commandParameters");

		dataReader = this.executeReader(connection, savepoint, commandType, commandText, commandParameters, COMMAND_BEHAVIOR, COMMAND_TIMEOUT, COMMAND_PREPARE);
		failFastOnlyWhen(dataReader == null, "dataReader == null");
		__watching(__, dataReader);

		results = this.getSchemaResultsFromDataReader(dataReader);
		failFastOnlyWhen(results == null, "results == null");
		__watching(__, results);

		results = new AbstractForEachYieldIterator<JdbcStreamingResult, JdbcStreamingResult>(results, (index, item) -> item)
		{
			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				if (dataReader != null)
				{
					dataReader.dispose();
				}

				super.dispose(disposing);
			}
		};

		__watching(__, results);
		__leave(__);

		return results;
	}

	@Override
	public LifecycleIterator<JdbcStreamingRecord> getRecordsFromDataReader(JdbcStreamingDataReader dataReader, RecordsAffectedCallback recordsAffectedCallback) throws Exception
	{
		final UUID __ = __enter();
		LifecycleIterator<JdbcStreamingRecord> records;

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// Note that THE DATA READER WILL NOT BE DISPOSED UPON ITERATION BY DESIGN...
		records = new AbstractYieldIterator<JdbcStreamingRecord>()
		{
			@Override
			protected void create(boolean creating) throws Exception
			{
				// do nothing
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				// do nothing
			}

			@Override
			protected Iterator<JdbcStreamingRecord> newIterator(int state)
			{
				// do nothing
				return this;
			}

			@Override
			protected boolean onTryYield(TryOut<JdbcStreamingRecord> value) throws Exception
			{
				JdbcStreamingRecordImpl record;

				if (value == null)
					throw new ArgumentNullException("value");

				failFastOnlyWhen(dataReader == null, "dataReader == null");

				if (!dataReader.readRecord())
					return false;

				record = new JdbcStreamingRecordImpl();
				record.setResultIndex(dataReader.getResultIndex());
				record.setRecordIndex(dataReader.getRecordIndex());

				//System.out.println(dataReader.getFieldCount());

				for (int fieldIndex = 0; fieldIndex < dataReader.getFieldCount(); fieldIndex++)
				{
					String key = dataReader.getName(fieldIndex);
					final Object value_ = dataReader.getValue(fieldIndex);

					if (record.containsKey(key) || Utils.isNullOrEmptyString(key))
						key = String.format("Field_%s", fieldIndex);

					record.put(key, value_);
				}

				value.setValue(record);
				return true;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{
				int recordsAffected;

				failFastOnlyWhen(dataReader == null, "dataReader == null");

				recordsAffected = dataReader.getRecordsAffected();

				if (recordsAffectedCallback != null)
					recordsAffectedCallback.invoke(recordsAffected);
			}

			@Override
			protected void onYieldFault(Exception ex)
			{
				// do nothing
			}

			@Override
			protected void onYieldResume() throws Exception
			{
				// do nothing
			}

			@Override
			protected void onYieldReturn(JdbcStreamingRecord value) throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");
			}

			@Override
			protected void onYieldStart() throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");
			}
		};

		__watching(__, records);
		__leave(__);

		return records;
	}

	@Override
	public LifecycleIterator<JdbcStreamingResult> getResultsFromDataReader(JdbcStreamingDataReader dataReader) throws Exception
	{
		final UUID __ = __enter();
		LifecycleIterator<JdbcStreamingResult> results;

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// Note that THE DATA READER WILL NOT BE DISPOSED UPON ITERATION BY DESIGN...
		results = getResultsFromDataReaderInternal(dataReader, false);
		failFastOnlyWhen(results == null, "results == null");
		__watching(__, results);

		__leave(__);

		return results;
	}

	private LifecycleIterator<JdbcStreamingResult> getResultsFromDataReaderInternal(JdbcStreamingDataReader dataReader, boolean schemaOnly)
	{
		final UUID __ = __enter();
		LifecycleIterator<JdbcStreamingResult> results;

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// Note that THE DATA READER WILL NOT BE DISPOSED UPON ITERATION BY DESIGN...
		results = new AbstractYieldIterator<JdbcStreamingResult>()
		{
			private boolean postCheckHasResults = true;

			@Override
			protected void create(boolean creating) throws Exception
			{
				// do nothing
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				// do nothing
			}

			@Override
			protected Iterator<JdbcStreamingResult> newIterator(int state)
			{
				// do nothing
				return this;
			}

			@Override
			protected boolean onTryYield(TryOut<JdbcStreamingResult> value) throws Exception
			{
				JdbcStreamingResultImpl result;
				LifecycleIterator<JdbcStreamingRecord> records;

				if (value == null)
					throw new ArgumentNullException("value");

				failFastOnlyWhen(dataReader == null, "dataReader == null");

				if (!this.postCheckHasResults)
					return false;

				result = new JdbcStreamingResultImpl();
				result.setResultIndex(dataReader.getResultIndex());

				if (schemaOnly)
					records = JdbcStreamingFascadeImpl.this.getSchemaRecordsFromDataReader(dataReader, (ra) -> result.setRecordsAffected(ra));
				else
					records = JdbcStreamingFascadeImpl.this.getRecordsFromDataReader(dataReader, (ra) -> result.setRecordsAffected(ra));

				failFastOnlyWhen(records == null, "records == null");

				result.setRecords(records);

				value.setValue(result);
				return true;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");
			}

			@Override
			protected void onYieldFault(Exception ex)
			{
				// do nothing
			}

			@Override
			protected void onYieldResume() throws Exception
			{
				this.postCheckHasResults = dataReader.nextResult(); // post-check
			}

			@Override
			protected void onYieldReturn(JdbcStreamingResult value) throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");
			}

			@Override
			protected void onYieldStart() throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");
			}
		};

		__watching(__, results);
		__leave(__);

		return results;
	}

	@Override
	public LifecycleIterator<JdbcStreamingRecord> getSchemaRecordsFromDataReader(JdbcStreamingDataReader dataReader, RecordsAffectedCallback recordsAffectedCallback) throws Exception
	{
		final UUID __ = __enter();
		LifecycleIterator<JdbcStreamingRecord> records;

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// Note that THE DATA READER WILL NOT BE DISPOSED UPON ITERATION BY DESIGN...
		records = new AbstractYieldIterator<JdbcStreamingRecord>()
		{
			private List<JdbcStreamingColumn> columns;
			private List<JdbcStreamingParameter> parameters;
			private long recordIndex = -1; // required in this implementation due to "pivot" of columns

			@Override
			protected void create(boolean creating) throws Exception
			{
				// do nothing
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				// do nothing
			}

			@Override
			protected Iterator<JdbcStreamingRecord> newIterator(int state)
			{
				// do nothing
				return this;
			}

			@Override
			protected boolean onTryYield(TryOut<JdbcStreamingRecord> value) throws Exception
			{
				JdbcStreamingRecordImpl record;

				if (value == null)
					throw new ArgumentNullException("value");

				failFastOnlyWhen(dataReader == null, "dataReader == null");

				final int recordIndex = (int) ++this.recordIndex;

				failFastOnlyWhen(recordIndex < 0, "recordIndex < 0");

				if (recordIndex >= this.columns.size() || this.columns.size() == 0)
					return false;

				final JdbcStreamingColumn column = this.columns.get(recordIndex);

				failFastOnlyWhen(column == null, "column == null");

				record = new JdbcStreamingRecordImpl();
				record.put(JdbcStreamingColumnImpl.COLUMN_NAME, column.getColumnName());
				record.put(JdbcStreamingColumnImpl.DATA_CLASS, column.getDataClass());
				record.put(JdbcStreamingColumnImpl.IS_KEY, column.isKey());
				record.put(JdbcStreamingColumnImpl.IS_NULLABLE, column.isNullable());

				failFastOnlyWhen(record == null, "record == null");

				record.setResultIndex(dataReader.getResultIndex());
				record.setRecordIndex(recordIndex);

				value.setValue(record);
				return true;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{
				int recordsAffected;

				failFastOnlyWhen(dataReader == null, "dataReader == null");

				this.columns = null;
				recordsAffected = dataReader.getRecordsAffected();

				if (recordsAffectedCallback != null)
					recordsAffectedCallback.invoke(recordsAffected);
			}

			@Override
			protected void onYieldFault(Exception ex)
			{
				// do nothing
			}

			@Override
			protected void onYieldResume() throws Exception
			{
				// do nothing
			}

			@Override
			protected void onYieldReturn(JdbcStreamingRecord value) throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");
			}

			@Override
			protected void onYieldStart() throws Exception
			{
				failFastOnlyWhen(dataReader == null, "dataReader == null");

				this.columns = dataReader.getColumnSchema();

				failFastOnlyWhen(this.columns == null, "this.columns == null");

				this.parameters = dataReader.getParameterSchema();

				failFastOnlyWhen(this.parameters == null, "this.parameters == null");
			}
		};

		__watching(__, records);
		__leave(__);

		return records;
	}

	@Override
	public LifecycleIterator<JdbcStreamingResult> getSchemaResultsFromDataReader(JdbcStreamingDataReader dataReader) throws Exception
	{
		final UUID __ = __enter();
		LifecycleIterator<JdbcStreamingResult> results;

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// Note that THE DATA READER WILL NOT BE DISPOSED UPON ITERATION BY DESIGN...
		results = this.getResultsFromDataReaderInternal(dataReader, true);
		failFastOnlyWhen(results == null, "results == null");
		__watching(__, results);

		__leave(__);

		return results;
	}
}

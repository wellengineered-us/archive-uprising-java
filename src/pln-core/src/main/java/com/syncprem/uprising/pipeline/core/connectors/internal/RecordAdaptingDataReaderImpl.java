/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors.internal;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.IndexOutOfRangeException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.streamingio.primitives.Field;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.primitives.Schema;
import com.syncprem.uprising.streamingio.relational.AbstractJdbcStreamingDataReader;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingColumn;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingColumnImpl;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingParameter;

import java.util.*;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.*;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class RecordAdaptingDataReaderImpl extends AbstractJdbcStreamingDataReader
{
	private RecordAdaptingDataReaderImpl(LifecycleIterator<Record> records)
	{
		if (records == null)
			throw new ArgumentNullException("records");

		this.records = records;
	}

	private final LifecycleIterator<Record> records;
	private Record record;

	@Override
	public List<JdbcStreamingColumn> getColumnSchema() throws Exception
	{
		List<JdbcStreamingColumn> columns;
		JdbcStreamingColumnImpl column;

		columns = new LinkedList<>();

		final Record record = this.getRecord();

		if (record == null)
			return null;

		final Schema schema = record.getSchema();

		if (schema == null)
			return null;

		final Map<String, Field> fields = schema.getFields();

		if (fields == null)
			return null;

		for (Map.Entry<String, Field> entry : fields.entrySet())
		{
			if (entry == null)
				continue;

			final Field field = entry.getValue();

			failFastOnlyWhen(field == null, "field == null");

			column = new JdbcStreamingColumnImpl();
			column.setAliased(false);
			column.setNullable(field.isFieldOptional());
			column.setAutoIncrement(false);
			column.setBaseCatalogName(null);
			column.setBaseColumnName(field.getFieldName());
			column.setBaseSchemaName(null);
			column.setBaseServerName(null);
			column.setBaseTableName(null);
			column.setColumnName(field.getFieldName());
			column.setColumnOrdinal((int) field.getFieldIndex());
			column.setColumnSize(0);
			column.setDataClass(field.getFieldClass());
			column.setDataClassName(field.getFieldClass() != null ? field.getFieldClass().getName() : null);
			column.setExpression(false);
			column.setHidden(false);
			column.setIdentity(false);
			column.setKey(field.isFieldKeyComponent());
			column.setLong(false);
			column.setPrecision(0);
			column.setScale(0);
			column.setReadOnly(false);
			column.setUdtQualifiedName(null);
			column.setUnique(false);

			columns.add(column);
		}

		return columns;
	}

	@Override
	public List<JdbcStreamingParameter> getParameterSchema() throws Exception
	{
		return null;
	}

	private Record getRecord()
	{
		return this.record;
	}

	private void setRecord(Record record)
	{
		this.record = record;
	}

	private LifecycleIterator<Record> getRecords()
	{
		return this.records;
	}

	public static RecordAdaptingDataReaderImpl create(LifecycleIterator<Record> records) throws Exception
	{
		final UUID __ = __enter();
		RecordAdaptingDataReaderImpl dataReader;

		if (records == null)
			throw new ArgumentNullException("records");

		dataReader = new RecordAdaptingDataReaderImpl(records);

		__watching(__, dataReader);

		if (!dataReader.nextResult())
			throw new InvalidOperationException(""); // move to first result from execute()...

		return __leave(__, dataReader);
	}

	private void assertStateOnNext()
	{
		if (this.getRecords() == null)
			throw new InvalidOperationException("");
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			// do nothing
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			if (this.getRecords() != null)
				this.getRecords().dispose();

			this.setColumnNamesByIndexZeroBased(null);
		}
	}

	@Override
	public Object getValue(String columnName) throws Exception
	{
		if (columnName == null)
			throw new ArgumentNullException("columnName");

		final int columnIndex = this.getOrdinal(columnName);

		if (columnIndex == DEFAULT_INDEX_INT)
			throw new IndexOutOfRangeException("columnName");

		final Record record = this.getRecord();

		if (record == null)
			return null;

		final Payload payload = record.getPayload();

		if (payload == null)
			return null;

		final Object value = payload.get(columnName);

		return value;
	}

	@Override
	protected boolean hasNextRecord() throws Exception
	{
		return this.getRecords() != null &&
				this.getRecords().hasNext();
	}

	@Override
	protected boolean hasNextResult() throws Exception
	{
		// single result only
		if (this.getResultIndex() > DEFAULT_INDEX)
			return false;
		else
			return true;
	}

	@Override
	public boolean nextResult() throws Exception
	{
		final UUID __ = __enter();
		boolean hasNext;

		this.assertStateOnNext();

		if (hasNext = this.hasNextResult())
		{
			// simply advance once - single result implementation
			this.incrementResultIndex();
			this.setRecordIndex(DEFAULT_INDEX); // reset for new result
		}
		else
		{
			// do not modify result set or record indices if no next result
		}

		return __leave(__, hasNext);
	}

	@Override
	public boolean readRecord() throws Exception
	{
		final UUID __ = __enter();
		boolean hasNext;

		this.assertStateOnNext();

		if (hasNext = this.hasNextRecord())
		{
			final Record record = this.getRecords().next();

			failFastOnlyWhen(record == null, "record == null");

			this.incrementRecordIndex();
			this.setRecord(record);

			this.updateColumnNameIndex();
		}
		else
		{
			// do not modify record index if no next record

			this.setColumnNamesByIndexZeroBased(null);
		}

		return __leave(__, hasNext);
	}

	private void updateColumnNameIndex()
	{
		String[] fieldNames;

		this.setColumnNamesByIndexZeroBased(null);

		final Record record = this.getRecord();

		if (record == null)
			return;

		final Schema schema = record.getSchema();

		if (schema == null)
			return;

		final Map<String, Field> fields = schema.getFields();

		if (fields == null)
			return;

		final Set<Map.Entry<String, Field>> entrySet = fields.entrySet();
		final int size = entrySet.size();

		fieldNames = new String[size];

		for (Map.Entry<String, Field> entry : entrySet)
		{
			if (entry == null)
				continue;

			final Field field = entry.getValue();
			final int fieldIndex = (int) field.getFieldIndex();
			final String fieldName = field.getFieldName();

			if (fieldIndex < size)
				fieldNames[fieldIndex] = fieldName;
		}

		this.setColumnNamesByIndexZeroBased(fieldNames);
	}
}

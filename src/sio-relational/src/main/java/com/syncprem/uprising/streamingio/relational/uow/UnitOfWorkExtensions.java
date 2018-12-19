/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational.uow;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.streamingio.relational.*;

import java.sql.JDBCType;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.getValueOrDefault;

public final class UnitOfWorkExtensions
{
	public static JdbcStreamingColumn createColumn(UnitOfWork unitOfWork, String columnSource, JDBCType columnType, Integer columnSize, Byte columnPrecision, Byte columnScale, Boolean columnNullable, String columnName) throws Exception
	{
		JdbcStreamingColumn column;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		column = LazyHolder.getInstance().createColumn(columnSource, getValueOrDefault(columnType, JDBCType.JAVA_OBJECT), getValueOrDefault(columnSize, 0), getValueOrDefault(columnPrecision, (byte) 0), getValueOrDefault(columnScale, (byte) 0), getValueOrDefault(columnNullable, true), columnName);

		return column;
	}

	public static JdbcStreamingParameter createParameter(UnitOfWork unitOfWork, String columnSource, ParameterDirection parameterDirection, JDBCType parameterType, Integer parameterSize, Byte parameterPrecision, Byte parameterScale, Boolean parameterNullable, String parameterName, Object parameterValue) throws Exception
	{
		JdbcStreamingParameter parameter;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		parameter = LazyHolder.getInstance().createParameter(columnSource, getValueOrDefault(parameterDirection, ParameterDirection.IN), getValueOrDefault(parameterType, JDBCType.JAVA_OBJECT), getValueOrDefault(parameterSize, 0), getValueOrDefault(parameterPrecision, (byte) 0), getValueOrDefault(parameterScale, (byte) 0), getValueOrDefault(parameterNullable, true), parameterName, parameterValue);

		return parameter;
	}

	public static LifecycleIterator<JdbcStreamingRecord> executeRecords(UnitOfWork unitOfWork, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, RecordsAffectedCallback rowsAffectedCallback) throws Exception
	{
		LifecycleIterator<JdbcStreamingRecord> records;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		records = LazyHolder.getInstance().executeRecords(unitOfWork.getConnection(), unitOfWork.getSavepoint(), getValueOrDefault(commandType, CommandType.TEXT), commandText, commandParameters, rowsAffectedCallback);

		return records;
	}

	public static LifecycleIterator<JdbcStreamingResult> executeResults(UnitOfWork unitOfWork, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters) throws Exception
	{
		LifecycleIterator<JdbcStreamingResult> results;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		results = LazyHolder.getInstance().executeResults(unitOfWork.getConnection(), unitOfWork.getSavepoint(), getValueOrDefault(commandType, CommandType.TEXT), commandText, commandParameters);

		return results;
	}

	public static LifecycleIterator<JdbcStreamingRecord> executeSchemaRecords(UnitOfWork unitOfWork, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, RecordsAffectedCallback rowsAffectedCallback) throws Exception
	{
		LifecycleIterator<JdbcStreamingRecord> records;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		records = LazyHolder.getInstance().executeSchemaRecords(unitOfWork.getConnection(), unitOfWork.getSavepoint(), getValueOrDefault(commandType, CommandType.TEXT), commandText, commandParameters, rowsAffectedCallback);

		return records;
	}

	public static LifecycleIterator<JdbcStreamingResult> executeSchemaResults(UnitOfWork unitOfWork, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters) throws Exception
	{
		LifecycleIterator<JdbcStreamingResult> results;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		results = LazyHolder.getInstance().executeSchemaResults(unitOfWork.getConnection(), unitOfWork.getSavepoint(), getValueOrDefault(commandType, CommandType.TEXT), commandText, commandParameters);

		return results;
	}

	private static class LazyHolder
	{
		private static final JdbcStreamingFascade instance = new JdbcStreamingFascadeImpl();

		public static JdbcStreamingFascade getInstance()
		{
			return instance;
		}
	}
}

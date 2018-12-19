/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentOutOfRangeException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.*;

public final class JdbcStreamingDataReaderImpl extends AbstractJdbcStreamingDataReader
{
	private JdbcStreamingDataReaderImpl(Statement statement, boolean hasResult)
	{
		if (statement == null)
			throw new ArgumentNullException("statement");

		this.statement = statement;
		this.hasResult = hasResult;
	}

	private final boolean hasResult;
	private final Statement statement;
	private ResultSet resultSet;

	@Override
	public List<JdbcStreamingColumn> getColumnSchema() throws Exception
	{
		List<JdbcStreamingColumn> columns;
		ResultSetMetaData resultSetMetaData = null;
		JdbcStreamingColumnImpl column;

		columns = new LinkedList<>();

		if (this.getStatement() != null &&
				this.getStatement() instanceof PreparedStatement)
		{
			final PreparedStatement preparedStatement = ((PreparedStatement) this.getStatement());
			resultSetMetaData = preparedStatement.getMetaData();
		}
		else if (this.getResultSet() != null)
		{
			resultSetMetaData = this.getResultSet().getMetaData();
		}

		if (resultSetMetaData != null)
		{
			for (int columnIndex = 0; columnIndex < resultSetMetaData.getColumnCount(); columnIndex++)
			{
				final int metaIndex = columnIndex + 1; // offset to JDBC one-based goofiness

				column = new JdbcStreamingColumnImpl();
				column.setAliased(null);
				column.setNullable(resultSetMetaData.isNullable(metaIndex) != ResultSetMetaData.columnNoNulls);
				column.setAutoIncrement(resultSetMetaData.isAutoIncrement(metaIndex));
				column.setBaseCatalogName(resultSetMetaData.getCatalogName(metaIndex));
				column.setBaseColumnName(resultSetMetaData.getColumnName(metaIndex));
				column.setBaseSchemaName(resultSetMetaData.getSchemaName(metaIndex));
				column.setBaseServerName(null);
				column.setBaseTableName(resultSetMetaData.getTableName(metaIndex));
				column.setColumnName(resultSetMetaData.getColumnName(metaIndex));
				column.setColumnOrdinal(metaIndex);
				column.setColumnSize(resultSetMetaData.getColumnDisplaySize(metaIndex));
				column.setDataClass(Utils.loadClassByName(resultSetMetaData.getColumnClassName(metaIndex)));
				column.setDataClassName(resultSetMetaData.getColumnClassName(metaIndex));
				column.setExpression(!resultSetMetaData.isWritable(metaIndex));
				column.setHidden(null);
				column.setIdentity(null);
				column.setKey(false);
				column.setLong(null);
				column.setPrecision(resultSetMetaData.getPrecision(metaIndex));
				column.setScale(resultSetMetaData.getScale(metaIndex));
				column.setReadOnly(resultSetMetaData.isReadOnly(metaIndex));
				column.setUdtQualifiedName(null);
				column.setUnique(null);
				column.setJdbcType(JDBCType.valueOf(resultSetMetaData.getColumnType(metaIndex)));
				columns.add(column);
			}
		}

		return columns;
	}

	@Override
	public List<JdbcStreamingParameter> getParameterSchema() throws Exception
	{
		List<JdbcStreamingParameter> parameters;
		ParameterMetaData parameterMetaData = null;
		JdbcStreamingParameterImpl parameter;

		parameters = new LinkedList<>();

		if (this.getStatement() != null &&
				this.getStatement() instanceof PreparedStatement)
		{
			final PreparedStatement preparedStatement = ((PreparedStatement) this.getStatement());
			parameterMetaData = preparedStatement.getParameterMetaData();
		}

		if (parameterMetaData != null)
		{
			for (int parameterIndex = 0; parameterIndex < parameterMetaData.getParameterCount(); parameterIndex++)
			{
				final int metaIndex = parameterIndex + 1; // offset to JDBC one-based goofiness

				parameter = new JdbcStreamingParameterImpl();
				parameter.setParameterOrdinal(metaIndex);
				parameter.setNullable(parameterMetaData.isNullable(metaIndex) != ParameterMetaData.parameterNoNulls);
				parameter.setJdbcType(JDBCType.valueOf(parameterMetaData.getParameterType(metaIndex)));
				parameter.setParameterName(String.format("#%s", parameterIndex));

				ParameterDirection direction;
				switch (parameterMetaData.getParameterMode(metaIndex))
				{
					case ParameterMetaData.parameterModeIn:
						direction = ParameterDirection.IN;
						break;
					case ParameterMetaData.parameterModeOut:
						direction = ParameterDirection.OUT;
						break;
					case ParameterMetaData.parameterModeInOut:
						direction = ParameterDirection.IN_OUT;
						break;
					default:
						direction = ParameterDirection.UNKNOWN;
						break;
				}

				parameter.setDirection(direction);
				parameter.setPrecision((byte) parameterMetaData.getPrecision(metaIndex));
				parameter.setScale((byte) parameterMetaData.getScale(metaIndex));
				parameter.setSize(null);
				parameter.setSourceColumn(null);
				parameter.setValue(null);

				parameters.add(parameter);
			}
		}

		return parameters;
	}

	@Override
	public int getRecordsAffected() throws Exception
	{
		if (this.getStatement() != null)
			return this.getStatement().getUpdateCount();
		else
			return super.getRecordsAffected();
	}

	private ResultSet getResultSet()
	{
		return this.resultSet;
	}

	private void setResultSet(ResultSet resultSet)
	{
		this.resultSet = resultSet;
	}

	private Statement getStatement()
	{
		return this.statement;
	}

	public static JdbcStreamingDataReader create(Connection connection, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, CommandBehavior commandBehavior, Integer commandTimeout, boolean commandPrepare) throws Exception
	{
		final UUID __ = __enter();
		boolean commandExecute;
		boolean hasResultSet = false;
		Statement statement;
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

		commandPrepare = commandPrepare || (commandBehavior.hasFlag(CommandBehavior.KEY_INFO) ||
				commandBehavior.hasFlag(CommandBehavior.SCHEMA_ONLY));

		commandExecute = !((commandBehavior.hasFlag(CommandBehavior.KEY_INFO) ||
				commandBehavior.hasFlag(CommandBehavior.SCHEMA_ONLY))) && commandType != CommandType.TABLE_DIRECT;

		switch (commandType)
		{
			case TEXT:
				if (!commandPrepare)
				{
					statement = connection.createStatement();

					// set timeout
					if (commandTimeout != null)
						statement.setQueryTimeout(commandTimeout.intValue());

					// do the database work
					if (commandExecute)
						hasResultSet = statement.execute(commandText);
				}
				else
				{
					PreparedStatement preparedStatement;

					statement = preparedStatement = connection.prepareStatement(commandText);

					// set timeout
					if (commandTimeout != null)
						preparedStatement.setQueryTimeout(commandTimeout.intValue());

					// add parameters
					if (commandParameters != null)
					{
						int parameterIndex = DEFAULT_INDEX_INT;
						for (JdbcStreamingParameter commandParameter : commandParameters)
						{
							if (commandParameter == null)
								continue;

							final ParameterDirection parameterDirection = Utils.getValueOrDefault(commandParameter.getDirection(), ParameterDirection.IN);

							if (parameterDirection != ParameterDirection.IN &&
									parameterDirection != ParameterDirection.IN_OUT)
								continue;

							preparedStatement.setObject((++parameterIndex) + 1 /* JDBC one-based fix-up */, commandParameter.getValue(), commandParameter.getJdbcType());
						}
					}

					// do the database work
					if (commandExecute)
						hasResultSet = preparedStatement.execute();
				}

				break;
			case STORED_PROCEDURE:
				// commandPrepare is mute here...
				CallableStatement callableStatement;

				statement = callableStatement = connection.prepareCall(commandText);

				// set timeout
				if (commandTimeout != null)
					callableStatement.setQueryTimeout(commandTimeout.intValue());

				// add parameters
				if (commandParameters != null)
				{
					int inParameterIndex = DEFAULT_INDEX_INT;
					int outParameterIndex = DEFAULT_INDEX_INT;
					for (JdbcStreamingParameter commandParameter : commandParameters)
					{
						if (commandParameter == null)
							continue;

						final ParameterDirection parameterDirection = Utils.getValueOrDefault(commandParameter.getDirection(), ParameterDirection.IN);

						if (parameterDirection == ParameterDirection.IN ||
								parameterDirection == ParameterDirection.IN_OUT)
							callableStatement.setObject((++inParameterIndex) + 1 /* JDBC one-based fix-up */, commandParameter.getValue(), commandParameter.getJdbcType());

						if (parameterDirection == ParameterDirection.OUT ||
								parameterDirection == ParameterDirection.IN_OUT)
							callableStatement.registerOutParameter((++outParameterIndex) + 1 /* JDBC one-based fix-up */, commandParameter.getJdbcType());
					}
				}

				// do the database work
				if (commandExecute)
					hasResultSet = callableStatement.execute();

				break;
			case TABLE_DIRECT:

				commandText = String.format("SELECT * FROM %s", commandText);

				if (!commandPrepare)
				{
					statement = connection.createStatement();

					// set timeout
					if (commandTimeout != null)
						statement.setQueryTimeout(commandTimeout.intValue());

					// do the database work
					if (commandExecute)
						hasResultSet = statement.execute(commandText);
				}
				else
				{
					PreparedStatement preparedStatement;

					statement = preparedStatement = connection.prepareStatement(commandText);

					// set timeout
					if (commandTimeout != null)
						preparedStatement.setQueryTimeout(commandTimeout.intValue());

					// add parameters
					if (commandParameters != null)
					{
						int parameterIndex = DEFAULT_INDEX_INT;
						for (JdbcStreamingParameter commandParameter : commandParameters)
						{
							if (commandParameter == null)
								continue;

							final ParameterDirection parameterDirection = Utils.getValueOrDefault(commandParameter.getDirection(), ParameterDirection.IN);

							if (parameterDirection != ParameterDirection.IN &&
									parameterDirection != ParameterDirection.IN_OUT)
								continue;

							preparedStatement.setObject((++parameterIndex) + 1 /* JDBC one-based fix-up */, commandParameter.getValue(), commandParameter.getJdbcType());
						}
					}

					// do the database work
					if (commandExecute)
						hasResultSet = preparedStatement.execute();
				}

				break;
			default:
				throw new ArgumentOutOfRangeException(commandType.toString());
		}

		//__wrap(__, statement);

		dataReader = new JdbcStreamingDataReaderImpl(statement, hasResultSet);

		__watching(__, dataReader);

		if (!dataReader.nextResult() && hasResultSet)
			throw new InvalidOperationException(""); // move to first result from execute()...

		return __leave(__, dataReader);
	}

	private void assertStateOnNext()
	{
		if (this.getResultIndex() == DEFAULT_INDEX)
			return;

		if (this.getStatement() == null)
			throw new InvalidOperationException("");

		//if (this.getResultSet() == null)
		//throw new InvalidOperationException("");
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
			if (this.getResultSet() != null)
			{
				this.getResultSet().close();
				this.setResultSet(null);
			}

			if (this.getStatement() != null)
				this.getStatement().close();
		}
	}

	@Override
	public Object getValue(String columnName) throws Exception
	{
		final Object value = this.getResultSet().getObject(columnName);
		return value;
	}

	@Override
	protected boolean hasNextRecord() throws Exception
	{
		return (this.getResultSet() != null &&
				this.getResultSet().next());
	}

	@Override
	protected boolean hasNextResult() throws Exception
	{
		boolean hasNextResult;
		hasNextResult = this.getStatement() != null &&
				this.getStatement().getResultSet() != null &&
				(this.getResultIndex() == DEFAULT_INDEX ||
						(this.getResultIndex() > DEFAULT_INDEX && this.getStatement().getMoreResults()));

		if (hasNextResult && !this.hasResult())
			throw new InvalidOperationException("");

		return hasNextResult;
	}

	private boolean hasResult()
	{
		return this.hasResult;
	}

	@Override
	public boolean nextResult() throws Exception
	{
		final UUID __ = __enter();
		ResultSet resultSet;
		boolean result;

		this.assertStateOnNext();

		if (result = this.hasNextResult())
		{
			if (this.getResultSet() != null)
				this.getResultSet().close();

			resultSet = this.getStatement().getResultSet();
			//__wrap(__, resultSet);

			this.setResultSet(resultSet);
			this.incrementResultIndex();
			this.setRecordIndex(DEFAULT_INDEX); // reset for new result

			this.updateColumnNameIndex();
		}
		else
		{
			// do not modify result set or record indices if no next result

			this.setColumnNamesByIndexZeroBased(null);
		}

		return __leave(__, result);
	}

	@Override
	public boolean readRecord() throws Exception
	{
		final UUID __ = __enter();
		boolean result;

		this.assertStateOnNext();

		if (result = this.hasNextRecord())
		{
			this.incrementRecordIndex();
		}
		else
		{
			// do not modify record index if no next record
		}

		return __leave(__, result);
	}

	private void updateColumnNameIndex() throws Exception
	{
		String[] fieldNames;

		this.setColumnNamesByIndexZeroBased(null);

		final ResultSet resultSet = this.getResultSet();

		if (resultSet == null)
			return;

		final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

		if (resultSetMetaData == null)
			return;

		fieldNames = new String[resultSetMetaData.getColumnCount()];

		for (int columnIndex = 0; columnIndex < resultSetMetaData.getColumnCount(); columnIndex++)
		{
			final int metaIndex = columnIndex + 1; // offset to JDBC one-based goofiness

			fieldNames[columnIndex] = resultSetMetaData.getColumnName(metaIndex);
		}

		this.setColumnNamesByIndexZeroBased(fieldNames);
	}
}

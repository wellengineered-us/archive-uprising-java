/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.Savepoint;

public interface JdbcStreamingFascade
{
	JdbcStreamingColumn createColumn(String sourceColumn, JDBCType columnType, int columnSize, byte columnPrecision, byte columnScale, boolean columnNullable, String columnName) throws Exception;

	JdbcStreamingParameter createParameter(String sourceColumn, ParameterDirection parameterDirection, JDBCType parameterType, int parameterSize, byte parameterPrecision, byte parameterScale, boolean parameterNullable, String parameterName, Object parameterValue) throws Exception;

	JdbcStreamingDataReader executeReader(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, CommandBehavior commandBehavior, Integer commandTimeout, boolean commandPrepare) throws Exception;

	LifecycleIterator<JdbcStreamingRecord> executeRecords(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, RecordsAffectedCallback recordsAffectedCallback) throws Exception;

	LifecycleIterator<JdbcStreamingResult> executeResults(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters) throws Exception;

	LifecycleIterator<JdbcStreamingRecord> executeSchemaRecords(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters, RecordsAffectedCallback recordsAffectedCallback) throws Exception;

	LifecycleIterator<JdbcStreamingResult> executeSchemaResults(Connection connection, Savepoint savepoint, CommandType commandType, String commandText, Iterable<JdbcStreamingParameter> commandParameters) throws Exception;

	LifecycleIterator<JdbcStreamingRecord> getRecordsFromDataReader(JdbcStreamingDataReader dataReader, RecordsAffectedCallback recordsAffectedCallback) throws Exception;

	LifecycleIterator<JdbcStreamingResult> getResultsFromDataReader(JdbcStreamingDataReader dataReader) throws Exception;

	LifecycleIterator<JdbcStreamingRecord> getSchemaRecordsFromDataReader(JdbcStreamingDataReader dataReader, RecordsAffectedCallback recordsAffectedCallback) throws Exception;

	LifecycleIterator<JdbcStreamingResult> getSchemaResultsFromDataReader(JdbcStreamingDataReader dataReader) throws Exception;
}

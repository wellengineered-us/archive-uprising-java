/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.relational.*;

import java.util.Iterator;

public final class SqlServerBulkCopyDestinationConnector extends AbstractJdbcDestinationConnector
{
	public SqlServerBulkCopyDestinationConnector()
	{
	}

	@Override
	protected void consumeInternal(Context context, RecordConfiguration configuration, JdbcStreamingDataReader dataReader) throws Exception
	{
		Iterator<JdbcStreamingResult> results;

		Iterable<JdbcStreamingParameter> parameters;

		long rowsCopied = 0;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// execute
		if (this.getSpecification().getExecuteCommand() != null &&
				this.getSpecification().getExecuteCommand().getCommandType() == CommandType.TABLE_DIRECT &&
				!Utils.isNullOrEmptyString(this.getSpecification().getExecuteCommand().getCommandText()))
		{
			try (SQLServerBulkCopy sqlServerBulkCopy = new SQLServerBulkCopy(this.getDestinationUnitOfWork().getConnection()))
			{
				if (this.getSpecification().getSqlServerBulkCopyOptions() != null)
					sqlServerBulkCopy.setBulkCopyOptions(this.getSpecification().getSqlServerBulkCopyOptions());

				sqlServerBulkCopy.setDestinationTableName(this.getSpecification().getExecuteCommand().getCommandText());

				final Iterable<JdbcStreamingColumn> columns = this.getSpecification().getExecuteCommand().getColumns(this.getDestinationUnitOfWork());
				final ISQLServerBulkRecord serverBulkRecord = new DataReaderAdaptingSqlServerBulkRecord(dataReader, columns);

				sqlServerBulkCopy.writeToServer(serverBulkRecord);
			}

			// cannot set rowsCopied yet
		}

		System.out.println(String.format("Rows copied = %s.", rowsCopied));
	}
}

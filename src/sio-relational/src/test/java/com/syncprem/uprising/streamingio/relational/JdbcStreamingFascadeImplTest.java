/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWork;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkExtensions;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkImpl;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.Savepoint;
import java.util.LinkedList;
import java.util.UUID;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.*;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public class JdbcStreamingFascadeImplTest
{
	public JdbcStreamingFascadeImplTest()
	{
	}

	@Test
	public void executeReader() throws Exception
	{
		JdbcStreamingFascade jdbcStreamingFascade;

		Connection connection;
		Savepoint savepoint;
		CommandType commandType;
		String commandText;
		Iterable<JdbcStreamingParameter> commandParameters;
		CommandBehavior commandBehavior;
		Integer commandTimeout;
		boolean commandPrepare;

		final UUID __ = __enter();

		jdbcStreamingFascade = new JdbcStreamingFascadeImpl();

		final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=bank_account_demo;user=bank_account_demo_mssql_lcl_login;password=LrJGmP6UfW8TEp7x3wWhECUYULE6zzMcWQ03R6UxeB4xzVmnq5S4Lx0vApegZVH";
		Class<? extends Driver> driverClass = Utils.loadClassByName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		UnitOfWork unitOfWork;
		try (var _0_ = __using(__, unitOfWork = UnitOfWorkImpl.create(driverClass, connectionUrl, true, IsolationLevel.NONE)))
		{
			connection = unitOfWork.getConnection();
			savepoint = unitOfWork.getSavepoint();
			commandType = CommandType.TEXT;
			commandText = "SELECT 1 as Bob UNION ALL SELECT 2 as Bob;SELECT 10 as Bill UNION ALL SELECT 20 as Bill;";
			commandParameters = new LinkedList<>();
			commandBehavior = CommandBehavior.DEFAULT;
			commandTimeout = null;
			commandPrepare = false;

			JdbcStreamingDataReader dataReader;
			try (var _1_ = __using(__, dataReader = jdbcStreamingFascade.executeReader(connection, savepoint, commandType, commandText, commandParameters, commandBehavior, commandTimeout, commandPrepare)))
			{
				do
				{
					while (dataReader.readRecord())
					{
						System.out.println(dataReader.getValue(0));
					}
				}
				while (dataReader.nextResult());
			}

			unitOfWork.complete();
		}

		__leave(__);

		__check();
	}

	@Test
	public void executeResults() throws Exception
	{
		CommandType commandType;
		String commandText;
		Iterable<JdbcStreamingParameter> commandParameters;

		final UUID __ = __enter();

		final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=bank_account_demo;user=bank_account_demo_mssql_lcl_login;password=LrJGmP6UfW8TEp7x3wWhECUYULE6zzMcWQ03R6UxeB4xzVmnq5S4Lx0vApegZVH";
		Class<? extends Driver> driverClass = Utils.loadClassByName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		UnitOfWork unitOfWork;
		try (var _0_ = __using(__, unitOfWork = UnitOfWorkImpl.create(driverClass, connectionUrl, true, IsolationLevel.NONE)))
		{
			commandType = CommandType.TEXT;
			commandText = "SELECT 1 as Bob UNION ALL SELECT 2 as Bob;SELECT 10 as Bill UNION ALL SELECT 20 as Bill;";
			commandParameters = new LinkedList<>();

			LifecycleIterator<JdbcStreamingResult> results;
			try (var _1_ = __using(__, results = UnitOfWorkExtensions.executeResults(unitOfWork, commandType, commandText, commandParameters)))
			{
				failFastOnlyWhen(results == null, "results == null");

				while (results.hasNext())
				{
					final JdbcStreamingResult result = results.next();

					failFastOnlyWhen(result == null, "result == null");

					final LifecycleIterator<JdbcStreamingRecord> records = result.getRecords();

					failFastOnlyWhen(records == null, "records == null");

					while (records.hasNext())
					{
						final JdbcStreamingRecord record = records.next();

						failFastOnlyWhen(record == null, "record == null");

						System.out.println(record);
					}
				}
			}

			unitOfWork.complete();
		}

		__leave(__);

		__check();
	}
}
/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.streamingio.relational.IsolationLevel;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWork;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkFactory;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkImpl;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

public class JdbcConnectorSpecificConfiguration extends StageSpecificConfiguration implements UnitOfWorkFactory
{
	public JdbcConnectorSpecificConfiguration()
	{
	}

	private String connectionUrl;
	private String driverClassName;
	private JdbcCommandConfiguration executeCommand;
	private JdbcCommandConfiguration postExecuteCommand;
	private JdbcCommandConfiguration preExecuteCommand;
	private SQLServerBulkCopyOptions sqlServerBulkCopyOptions;

	public String getConnectionUrl()
	{
		return this.connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl)
	{
		this.connectionUrl = connectionUrl;
	}

	public Class<? extends Driver> getDriverClass()
	{
		return Utils.loadClassByName(this.getDriverClassName(), Driver.class);
	}

	public String getDriverClassName()
	{
		return this.driverClassName;
	}

	public void setDriverClassName(String driverClassName)
	{
		this.driverClassName = driverClassName;
	}

	public JdbcCommandConfiguration getExecuteCommand()
	{
		return this.executeCommand;
	}

	public void setExecuteCommand(JdbcCommandConfiguration executeCommand)
	{
		this.ensureParentOnSetter(this.executeCommand, executeCommand);
		this.executeCommand = executeCommand;
	}

	public JdbcCommandConfiguration getPostExecuteCommand()
	{
		return this.postExecuteCommand;
	}

	public void setPostExecuteCommand(JdbcCommandConfiguration postExecuteCommand)
	{
		this.ensureParentOnSetter(this.postExecuteCommand, postExecuteCommand);
		this.postExecuteCommand = postExecuteCommand;
	}

	public JdbcCommandConfiguration getPreExecuteCommand()
	{
		return this.preExecuteCommand;
	}

	public void setPreExecuteCommand(JdbcCommandConfiguration preExecuteCommand)
	{
		this.ensureParentOnSetter(this.preExecuteCommand, preExecuteCommand);
		this.preExecuteCommand = preExecuteCommand;
	}

	public SQLServerBulkCopyOptions getSqlServerBulkCopyOptions()
	{
		return sqlServerBulkCopyOptions;
	}

	public void setSqlServerBulkCopyOptions(SQLServerBulkCopyOptions sqlServerBulkCopyOptions)
	{
		this.sqlServerBulkCopyOptions = sqlServerBulkCopyOptions;
	}

	public UnitOfWork getUnitOfWork() throws Exception
	{
		return this.getUnitOfWork(true, IsolationLevel.NONE);
	}

	public UnitOfWork getUnitOfWork(boolean transactional, IsolationLevel isolationLevel) throws Exception
	{
		return UnitOfWorkImpl.create(this.getDriverClass(), this.getConnectionUrl(), transactional, isolationLevel);
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		Class<? extends Driver> driverClass;
		Driver driver_;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getDriverClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s driver class name is required.", context), Severity.ERROR));
		else
		{
			driverClass = this.getDriverClass();

			if (driverClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to load driver class by name.", context), Severity.ERROR));
			else if (!Driver.class.isAssignableFrom(driverClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s loaded an unrecognized driver driver class by name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				driver_ = Utils.newObjectFromClass(driverClass);

				if (driver_ == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to instantiate driver driver class by name using default, public constructor.", context), Severity.ERROR));
			}
		}

		if (Utils.isNullOrEmptyString(this.getConnectionUrl()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector JDBC connection string is required.", context), Severity.ERROR));

		if (this.getPreExecuteCommand() != null)
			MessageImpl.addRange(messages, this.getPreExecuteCommand().validate("Pre-Execution"));

		if (this.getExecuteCommand() != null)
			MessageImpl.addRange(messages, this.getExecuteCommand().validate("Execution"));
		else
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector JDBC execute command is required.", context), Severity.ERROR));

		if (this.getPostExecuteCommand() != null)
			MessageImpl.addRange(messages, this.getPostExecuteCommand().validate("Post-Execution"));

		return messages;
	}
}

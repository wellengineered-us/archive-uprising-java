/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.configuration.AbstractConfigurationObject;
import com.syncprem.uprising.infrastructure.configuration.ConfigurationObjectCollectionImpl;
import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.streamingio.relational.CommandBehavior;
import com.syncprem.uprising.streamingio.relational.CommandType;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingColumn;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingParameter;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWork;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkExtensions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class JdbcCommandConfiguration extends AbstractConfigurationObject
{
	public JdbcCommandConfiguration()
	{
		this.parameterConfigurations = new ConfigurationObjectCollectionImpl<>(this);
		this.columnConfigurations = new ConfigurationObjectCollectionImpl<>(this);
	}

	protected JdbcCommandConfiguration(ConfigurationObjectCollectionImpl<JdbcParameterConfiguration> parameterConfigurations,
									   ConfigurationObjectCollectionImpl<JdbcColumnConfiguration> columnConfigurations)
	{
		if (parameterConfigurations == null)
			throw new ArgumentNullException("parameterConfigurations");

		if (columnConfigurations == null)
			throw new ArgumentNullException("columnConfigurations");

		this.parameterConfigurations = parameterConfigurations;
		this.columnConfigurations = columnConfigurations;
	}

	private final ConfigurationObjectCollectionImpl<JdbcColumnConfiguration> columnConfigurations;
	private final ConfigurationObjectCollectionImpl<JdbcParameterConfiguration> parameterConfigurations;
	private CommandBehavior commandBehavior = CommandBehavior.DEFAULT;
	private boolean commandPrepare;
	private String commandText;
	private Integer commandTimeout;
	private CommandType commandType;

	public ConfigurationObjectCollectionImpl<JdbcColumnConfiguration> getColumnConfigurations()
	{
		return this.columnConfigurations;
	}

	public CommandBehavior getCommandBehavior()
	{
		return this.commandBehavior;
	}

	public void setCommandBehavior(CommandBehavior commandBehavior)
	{
		this.commandBehavior = commandBehavior;
	}

	public boolean getCommandPrepare()
	{
		return this.commandPrepare;
	}

	public void setCommandPrepare(boolean commandPrepare)
	{
		this.commandPrepare = commandPrepare;
	}

	public String getCommandText()
	{
		return this.commandText;
	}

	public void setCommandText(String commandText)
	{
		this.commandText = commandText;
	}

	public Integer getCommandTimeout()
	{
		return this.commandTimeout;
	}

	public void setCommandTimeout(Integer commandTimeout)
	{
		this.commandTimeout = commandTimeout;
	}

	public CommandType getCommandType()
	{
		return this.commandType;
	}

	public void setCommandType(CommandType commandType)
	{
		this.commandType = commandType;
	}

	public ConfigurationObjectCollectionImpl<JdbcParameterConfiguration> getParameterConfigurations()
	{
		return this.parameterConfigurations;
	}

	public Iterable<JdbcStreamingColumn> getColumns(UnitOfWork unitOfWork) throws Exception
	{
		List<JdbcStreamingColumn> columns;
		JdbcStreamingColumn column;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		columns = new ArrayList<>();

		if (this.getColumnConfigurations() != null)
		{
			for (JdbcColumnConfiguration jdbcColumnConfiguration : this.getColumnConfigurations())
			{
				if (jdbcColumnConfiguration == null)
					continue;

				column = UnitOfWorkExtensions.createColumn(unitOfWork, jdbcColumnConfiguration.getSourceColumn(), jdbcColumnConfiguration.getColumnType(), jdbcColumnConfiguration.getColumnSize(), jdbcColumnConfiguration.getColumnPrecision(), jdbcColumnConfiguration.getColumnScale(), jdbcColumnConfiguration.getColumnNullable(), jdbcColumnConfiguration.getColumnName());
				columns.add(column);
			}
		}

		return columns;
	}

	public Iterable<JdbcStreamingParameter> getParameters(UnitOfWork unitOfWork) throws Exception
	{
		List<JdbcStreamingParameter> parameters;
		JdbcStreamingParameter parameter;

		if (unitOfWork == null)
			throw new ArgumentNullException("unitOfWork");

		parameters = new ArrayList<>();

		if (this.getParameterConfigurations() != null)
		{
			for (JdbcParameterConfiguration jdbcParameterConfiguration : this.getParameterConfigurations())
			{
				if (jdbcParameterConfiguration == null)
					continue;

				parameter = UnitOfWorkExtensions.createParameter(unitOfWork, jdbcParameterConfiguration.getSourceColumn(), jdbcParameterConfiguration.getParameterDirection(), jdbcParameterConfiguration.getParameterType(), jdbcParameterConfiguration.getParameterSize(), jdbcParameterConfiguration.getParameterPrecision(), jdbcParameterConfiguration.getParameterScale(), jdbcParameterConfiguration.getParameterNullable(), jdbcParameterConfiguration.getParameterName(), jdbcParameterConfiguration.getParameterValue());
				parameters.add(parameter);
			}
		}

		return parameters;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (this.getParameterConfigurations() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("JDBC parameter configurations are required."), Severity.ERROR));
		else
		{
			MessageImpl.addRange(messages, this.getParameterConfigurations().validateAll("JDBC parameter"));

			final Map<String, List<JdbcParameterConfiguration>> orderedGroups = this.getParameterConfigurations()
					.stream()
					.sorted(Comparator.comparing(JdbcParameterConfiguration::getParameterName))
					.collect(groupingBy(JdbcParameterConfiguration::getParameterName));

			// check for duplicate parameters
			for (Map.Entry<String, List<JdbcParameterConfiguration>> orderedGroup : orderedGroups.entrySet())
			{
				if (orderedGroup == null)
					continue;

				if (orderedGroup.getValue().size() > 1)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s has a duplicate JDBC parameter name defined: '%s'.", context, orderedGroup.getKey()), Severity.ERROR));
			}
		}

		if (this.getColumnConfigurations() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("JDBC column configurations are required."), Severity.ERROR));
		else
		{
			MessageImpl.addRange(messages, this.getColumnConfigurations().validateAll("JDBC column"));

			final Map<String, List<JdbcColumnConfiguration>> orderedGroups = this.getColumnConfigurations()
					.stream()
					.sorted(Comparator.comparing(JdbcColumnConfiguration::getColumnName))
					.collect(groupingBy(JdbcColumnConfiguration::getColumnName));

			// check for duplicate columns
			for (Map.Entry<String, List<JdbcColumnConfiguration>> orderedGroup : orderedGroups.entrySet())
			{
				if (orderedGroup == null)
					continue;

				if (orderedGroup.getValue().size() > 1)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s has a duplicate JDBC column name defined: '%s'.", context, orderedGroup.getKey()), Severity.ERROR));
			}
		}

		return messages;
	}
}

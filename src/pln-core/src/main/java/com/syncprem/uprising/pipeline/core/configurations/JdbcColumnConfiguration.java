/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.configuration.AbstractConfigurationObject;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

public class JdbcColumnConfiguration extends AbstractConfigurationObject
{
	public JdbcColumnConfiguration()
	{
	}

	private String columnName;
	private Boolean columnNullable;
	private Byte columnPrecision;
	private Byte columnScale;
	private Integer columnSize;
	private JDBCType columnType;
	private Object columnValue;
	private String sourceColumn;

	public String getColumnName()
	{
		return this.columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public Boolean getColumnNullable()
	{
		return this.columnNullable;
	}

	public void setColumnNullable(Boolean columnNullable)
	{
		this.columnNullable = columnNullable;
	}

	public Byte getColumnPrecision()
	{
		return this.columnPrecision;
	}

	public void setColumnPrecision(Byte columnPrecision)
	{
		this.columnPrecision = columnPrecision;
	}

	public Byte getColumnScale()
	{
		return this.columnScale;
	}

	public void setColumnScale(Byte columnScale)
	{
		this.columnScale = columnScale;
	}

	public Integer getColumnSize()
	{
		return this.columnSize;
	}

	public void setColumnSize(Integer columnSize)
	{
		this.columnSize = columnSize;
	}

	public JDBCType getColumnType()
	{
		return this.columnType;
	}

	public void setColumnType(JDBCType columnType)
	{
		this.columnType = columnType;
	}

	public Object getColumnValue()
	{
		return this.columnValue;
	}

	public void setColumnValue(Object columnValue)
	{
		this.columnValue = columnValue;
	}

	public String getSourceColumn()
	{
		return this.sourceColumn;
	}

	public void setSourceColumn(String sourceColumn)
	{
		this.sourceColumn = sourceColumn;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getColumnName()))
			messages.add(new MessageImpl("", String.format("Column[%s] name is required.", context), Severity.ERROR));

		return messages;
	}
}

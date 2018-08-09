/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.configuration.AbstractConfigurationObject;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.streamingio.relational.ParameterDirection;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

public class JdbcParameterConfiguration extends AbstractConfigurationObject
{
	public JdbcParameterConfiguration()
	{
	}

	private ParameterDirection parameterDirection;
	private String parameterName;
	private Boolean parameterNullable;
	private Byte parameterPrecision;
	private Byte parameterScale;
	private Integer parameterSize;
	private JDBCType parameterType;
	private Object parameterValue;
	private String sourceColumn;

	public ParameterDirection getParameterDirection()
	{
		return this.parameterDirection;
	}

	public void setParameterDirection(ParameterDirection parameterDirection)
	{
		this.parameterDirection = parameterDirection;
	}

	public String getParameterName()
	{
		return this.parameterName;
	}

	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	public Boolean getParameterNullable()
	{
		return this.parameterNullable;
	}

	public void setParameterNullable(Boolean parameterNullable)
	{
		this.parameterNullable = parameterNullable;
	}

	public Byte getParameterPrecision()
	{
		return this.parameterPrecision;
	}

	public void setParameterPrecision(Byte parameterPrecision)
	{
		this.parameterPrecision = parameterPrecision;
	}

	public Byte getParameterScale()
	{
		return this.parameterScale;
	}

	public void setParameterScale(Byte parameterScale)
	{
		this.parameterScale = parameterScale;
	}

	public Integer getParameterSize()
	{
		return this.parameterSize;
	}

	public void setParameterSize(Integer parameterSize)
	{
		this.parameterSize = parameterSize;
	}

	public JDBCType getParameterType()
	{
		return this.parameterType;
	}

	public void setParameterType(JDBCType parameterType)
	{
		this.parameterType = parameterType;
	}

	public Object getParameterValue()
	{
		return this.parameterValue;
	}

	public void setParameterValue(Object parameterValue)
	{
		this.parameterValue = parameterValue;
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

		if (Utils.isNullOrEmptyString(this.getParameterName()))
			messages.add(new MessageImpl("", String.format("Parameter[%s] name is required.", context), Severity.ERROR));

		return messages;
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import java.sql.JDBCType;

public final class JdbcStreamingParameterImpl implements JdbcStreamingParameter
{
	public JdbcStreamingParameterImpl()
	{
	}

	private ParameterDirection direction;
	private Boolean isNullable;
	private JDBCType jdbcType;
	private String parameterName;
	private Integer parameterOrdinal;
	private Byte precision;
	private Byte scale;
	private Integer size;
	private String sourceColumn;
	private Object value;

	@Override
	public ParameterDirection getDirection()
	{
		return this.direction;
	}

	public void setDirection(ParameterDirection direction)
	{
		this.direction = direction;
	}

	@Override
	public JDBCType getJdbcType()
	{
		return this.jdbcType;
	}

	public void setJdbcType(JDBCType jdbcType)
	{
		this.jdbcType = jdbcType;
	}

	@Override
	public String getParameterName()
	{
		return this.parameterName;
	}

	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	@Override
	public Integer getParameterOrdinal()
	{
		return this.parameterOrdinal;
	}

	public void setParameterOrdinal(Integer parameterOrdinal)
	{
		this.parameterOrdinal = parameterOrdinal;
	}

	@Override
	public Byte getPrecision()
	{
		return this.precision;
	}

	public void setPrecision(Byte precision)
	{
		this.precision = precision;
	}

	@Override
	public Byte getScale()
	{
		return this.scale;
	}

	public void setScale(Byte scale)
	{
		this.scale = scale;
	}

	@Override
	public Integer getSize()
	{
		return this.size;
	}

	public void setSize(Integer size)
	{
		this.size = size;
	}

	@Override
	public String getSourceColumn()
	{
		return this.sourceColumn;
	}

	public void setSourceColumn(String sourceColumn)
	{
		this.sourceColumn = sourceColumn;
	}

	@Override
	public Object getValue()
	{
		return this.value;
	}

	@Override
	public void setValue(Object value)
	{
		this.value = value;
	}

	public void setNullable(Boolean isNullable)
	{
		this.isNullable = isNullable;
	}

	@Override
	public Boolean isNullable()
	{
		return this.isNullable;
	}
}

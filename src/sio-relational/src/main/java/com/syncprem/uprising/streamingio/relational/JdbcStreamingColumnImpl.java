/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import java.sql.JDBCType;

public final class JdbcStreamingColumnImpl implements JdbcStreamingColumn
{
	public JdbcStreamingColumnImpl()
	{
	}

	public static final String COLUMN_NAME = "columnName";
	public static final String DATA_CLASS = "dataClass";
	public static final String IS_KEY = "isKey";
	public static final String IS_NULLABLE = "isNullable";
	private String baseCatalogName;
	private String baseColumnName;
	private String baseSchemaName;
	private String baseServerName;
	private String baseTableName;
	private String columnName;
	private Integer columnOrdinal;
	private Integer columnSize;
	private Class<?> dataClass;
	private String dataClassName;
	private Boolean isAliased;
	private Boolean isAutoIncrement;
	private Boolean isExpression;
	private Boolean isHidden;
	private Boolean isIdentity;
	private Boolean isKey;
	private Boolean isLong;
	private Boolean isNullable;
	private Boolean isReadOnly;
	private Boolean isUnique;
	private JDBCType jdbcType;
	private Integer precision;
	private Integer scale;
	private String sourceColumn;
	private String udtQualifiedName;

	@Override
	public String getBaseCatalogName()
	{
		return this.baseCatalogName;
	}

	public void setBaseCatalogName(String baseCatalogName)
	{
		this.baseCatalogName = baseCatalogName;
	}

	@Override
	public String getBaseColumnName()
	{
		return this.baseColumnName;
	}

	public void setBaseColumnName(String baseColumnName)
	{
		this.baseColumnName = baseColumnName;
	}

	@Override
	public String getBaseSchemaName()
	{
		return this.baseSchemaName;
	}

	public void setBaseSchemaName(String baseSchemaName)
	{
		this.baseSchemaName = baseSchemaName;
	}

	@Override
	public String getBaseServerName()
	{
		return this.baseServerName;
	}

	public void setBaseServerName(String baseServerName)
	{
		this.baseServerName = baseServerName;
	}

	@Override
	public String getBaseTableName()
	{
		return this.baseTableName;
	}

	public void setBaseTableName(String baseTableName)
	{
		this.baseTableName = baseTableName;
	}

	@Override
	public String getColumnName()
	{
		return this.columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	@Override
	public Integer getColumnOrdinal()
	{
		return this.columnOrdinal;
	}

	public void setColumnOrdinal(Integer columnOrdinal)
	{
		this.columnOrdinal = columnOrdinal;
	}

	@Override
	public Integer getColumnSize()
	{
		return this.columnSize;
	}

	public void setColumnSize(Integer columnSize)
	{
		this.columnSize = columnSize;
	}

	@Override
	public Class<?> getDataClass()
	{
		return this.dataClass;
	}

	public void setDataClass(Class<?> dataClass)
	{
		this.dataClass = dataClass;
	}

	@Override
	public String getDataClassName()
	{
		return this.dataClassName;
	}

	public void setDataClassName(String dataClassName)
	{
		this.dataClassName = dataClassName;
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
	public Integer getPrecision()
	{
		return this.precision;
	}

	public void setPrecision(Integer precision)
	{
		this.precision = precision;
	}

	@Override
	public Integer getScale()
	{
		return this.scale;
	}

	public void setScale(Integer scale)
	{
		this.scale = scale;
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
	public String getUdtQualifiedName()
	{
		return this.udtQualifiedName;
	}

	public void setUdtQualifiedName(String udtQualifiedName)
	{
		this.udtQualifiedName = udtQualifiedName;
	}

	public void setAliased(Boolean isAliased)
	{
		this.isAliased = isAliased;
	}

	public void setAutoIncrement(Boolean isAutoIncrement)
	{
		this.isAutoIncrement = isAutoIncrement;
	}

	public void setExpression(Boolean isExpression)
	{
		this.isExpression = isExpression;
	}

	public void setHidden(Boolean isHidden)
	{
		this.isHidden = isHidden;
	}

	public void setIdentity(Boolean isIdentity)
	{
		this.isIdentity = isIdentity;
	}

	public void setKey(Boolean isKey)
	{
		this.isKey = isKey;
	}

	public void setLong(Boolean isLong)
	{
		this.isLong = isLong;
	}

	public void setNullable(Boolean isNullable)
	{
		this.isNullable = isNullable;
	}

	public void setReadOnly(Boolean isReadOnly)
	{
		this.isReadOnly = isReadOnly;
	}

	public void setUnique(Boolean isUnique)
	{
		this.isUnique = isUnique;
	}

	@Override
	public Boolean isAliased()
	{
		return this.isAliased;
	}

	@Override
	public Boolean isAutoIncrement()
	{
		return this.isAutoIncrement;
	}

	@Override
	public Boolean isExpression()
	{
		return this.isExpression;
	}

	@Override
	public Boolean isHidden()
	{
		return this.isHidden;
	}

	@Override
	public Boolean isIdentity()
	{
		return this.isIdentity;
	}

	@Override
	public Boolean isKey()
	{
		return this.isKey;
	}

	@Override
	public Boolean isLong()
	{
		return this.isLong;
	}

	@Override
	public Boolean isNullable()
	{
		return this.isNullable;
	}

	@Override
	public Boolean isReadOnly()
	{
		return this.isReadOnly;
	}

	@Override
	public Boolean isUnique()
	{
		return this.isUnique;
	}
}

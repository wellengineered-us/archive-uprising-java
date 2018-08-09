/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import java.sql.JDBCType;

public interface JdbcStreamingColumn
{
	String getBaseCatalogName();

	String getBaseColumnName();

	String getBaseSchemaName();

	String getBaseServerName();

	String getBaseTableName();

	String getColumnName();

	Integer getColumnOrdinal();

	Integer getColumnSize();

	Class<?> getDataClass();

	String getDataClassName();

	JDBCType getJdbcType();

	Integer getPrecision();

	Integer getScale();

	String getSourceColumn();

	String getUdtQualifiedName();

	Boolean isAliased();

	Boolean isAutoIncrement();

	Boolean isExpression();

	Boolean isHidden();

	Boolean isIdentity();

	Boolean isKey();

	Boolean isLong();

	Boolean isNullable();

	Boolean isReadOnly();

	Boolean isUnique();
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import java.sql.JDBCType;

public interface JdbcStreamingParameter
{
	ParameterDirection getDirection();

	JDBCType getJdbcType();

	String getParameterName();

	Integer getParameterOrdinal();

	Byte getPrecision();

	Byte getScale();

	Integer getSize();

	String getSourceColumn();

	Object getValue();

	void setValue(Object value);

	Boolean isNullable();
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import java.util.Iterator;

public interface JdbcStreamingDataRecord
{
	int getFieldCount() throws Exception;

	JdbcStreamingDataReader getData(int columnIndexZeroBased) throws Exception;

	JdbcStreamingDataReader getData(String columnName) throws Exception;

	String getDataTypeName(int columnIndexZeroBased);

	String getDataTypeName(String columnName);

	Class<?> getFieldType(int columnIndexZeroBased);

	Class<?> getFieldType(String columnName);

	String getName(int columnIndexZeroBased) throws Exception;

	int getOrdinal(String columnName) throws Exception;

	Object getValue(int columnIndexZeroBased) throws Exception;

	Object getValue(String columnName) throws Exception;

	Iterator<Object> getValues(int columnIndexZeroBased) throws Exception;

	Iterator<Object> getValues(String columnName) throws Exception;

	boolean isDbNull(int columnIndexZeroBased);

	boolean isDbNull(String columnName);

	/* EXCLUDE TYPE SPECIFIC METHODS */
}

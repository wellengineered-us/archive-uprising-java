/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

public interface Field
{
	Class<?> getFieldClass();

	long getFieldIndex();

	String getFieldName();

	Schema getFieldSchema();

	boolean isFieldKeyComponent();

	boolean isFieldOptional();
}

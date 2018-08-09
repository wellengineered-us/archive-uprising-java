/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

import java.util.Map;

public interface Schema
{
	Map<String, Field> getFields();

	String getSchemaName();

	SchemaType getSchemaType();

	int getSchemaVersion();
}

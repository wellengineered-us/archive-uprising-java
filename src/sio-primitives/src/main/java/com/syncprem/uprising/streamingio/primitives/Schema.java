/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
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

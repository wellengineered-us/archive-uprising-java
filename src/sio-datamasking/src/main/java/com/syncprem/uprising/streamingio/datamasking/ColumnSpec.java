/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

import java.util.Map;

public interface ColumnSpec<TObfuscationStrategySpec>
{
	String getColumnName();

	void setColumnName(String columnName);

	Class<? extends ObfuscationStrategy> getObfuscationStrategyClass();

	void setObfuscationStrategyClass(Class<? extends ObfuscationStrategy> obfuscationStrategyClass);

	Map<String, Object> getObfuscationStrategySpec();

	void setObfuscationStrategySpec(Map<String, Object> obfuscationStrategySpec);
}

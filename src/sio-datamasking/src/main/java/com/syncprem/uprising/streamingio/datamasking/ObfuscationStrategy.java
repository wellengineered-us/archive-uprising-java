/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

import com.syncprem.uprising.streamingio.primitives.Field;

public interface ObfuscationStrategy
{
	Class<?> getObfuscationStrategySpecificSpecType();

	Object getObfuscatedValue(ObfuscationContext obfuscationContext, ColumnSpec<?> columnSpec, Field field, Object originalFieldValue);
}

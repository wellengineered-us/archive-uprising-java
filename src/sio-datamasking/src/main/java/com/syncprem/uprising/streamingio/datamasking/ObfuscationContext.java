/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

import com.syncprem.uprising.infrastructure.polyfills.TryOut;

public interface ObfuscationContext
{
	long getSignHash(Object value);

	long getValueHash(Long size, Object value);

	boolean tryGetSurrogateValue(DictionarySpec dictionarySpec, Object surrogateKey, TryOut<Object> tryOutSurrogateValue);
}

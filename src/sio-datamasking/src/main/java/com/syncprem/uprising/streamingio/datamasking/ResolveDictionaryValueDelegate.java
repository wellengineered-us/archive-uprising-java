/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

import com.syncprem.uprising.streamingio.primitives.SyncPremException;

@FunctionalInterface
public interface ResolveDictionaryValueDelegate
{
	Object invoke(DictionarySpec dictionarySpec, Object surrogateKey) throws SyncPremException;
}

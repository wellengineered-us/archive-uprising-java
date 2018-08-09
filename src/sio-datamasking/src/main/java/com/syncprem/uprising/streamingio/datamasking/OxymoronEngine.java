/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.streamingio.primitives.Field;
import com.syncprem.uprising.streamingio.primitives.Payload;

import java.util.Iterator;

public interface OxymoronEngine extends Creatable, Disposable
{
	Object getObfuscatedValue(Field field, Object originalFieldValue);

	Iterator<Payload> getObfuscatedValues(Iterator<Payload> records);
}

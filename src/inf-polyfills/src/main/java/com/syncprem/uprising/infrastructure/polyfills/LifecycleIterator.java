/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.Iterator;

public interface LifecycleIterator<T> extends Iterator<T>, Creatable, Disposable
{
}

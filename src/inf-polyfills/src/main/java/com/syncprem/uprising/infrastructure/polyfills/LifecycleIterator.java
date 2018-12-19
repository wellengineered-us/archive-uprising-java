/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.Iterator;

public interface LifecycleIterator<T> extends Iterator<T>, Creatable, Disposable
{
}

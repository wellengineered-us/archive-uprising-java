/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public interface Disposable extends AutoCloseable
{
	boolean isDisposed();

	void dispose() throws Exception;
}

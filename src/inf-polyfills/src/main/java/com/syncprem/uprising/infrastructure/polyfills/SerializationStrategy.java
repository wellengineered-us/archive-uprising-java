/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.io.OutputStream;

public interface SerializationStrategy
{
	void setObjectToStream(OutputStream outputStream, Object obj) throws Exception;
}
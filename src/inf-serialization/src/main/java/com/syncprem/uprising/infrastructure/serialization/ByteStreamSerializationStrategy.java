/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public interface ByteStreamSerializationStrategy
{
	<TObject> TObject deserializeObjectFromByteStream(Class<? extends TObject> clazz, InputStream inputStream) throws Exception;

	<TObject> void serializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> clazz, TObject obj) throws Exception;
}
/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

public interface BytesSerializationStrategy
{
	<TObject> TObject deserializeObjectFromBytes(Class<? extends TObject> clazz, byte[] inputBytes) throws Exception;

	<TObject> byte[] serializeObjectToBytes(/*byte[] outputBytes,*/ Class<? extends TObject> clazz, TObject obj) throws Exception;
}
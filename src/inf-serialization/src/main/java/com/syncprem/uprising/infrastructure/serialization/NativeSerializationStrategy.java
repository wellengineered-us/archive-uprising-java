/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

public interface NativeSerializationStrategy<TNativeInput, TNativeOutput>
{
	<TObject> TObject deserializeObjectFromNative(Class<? extends TObject> clazz, TNativeInput nativeInput) throws Exception;

	<TObject> void serializeObjectToNative(TNativeOutput nativeOutput, Class<? extends TObject> clazz, TObject obj) throws Exception;
}
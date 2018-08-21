/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

public interface FileSerializationStrategy
{
	<TObject> TObject deserializeObjectFromFile(Class<? extends TObject> clazz, String inputFilePath) throws Exception;

	<TObject> void serializeObjectToFile(String outputFilePath, Class<? extends TObject> clazz, TObject obj) throws Exception;
}
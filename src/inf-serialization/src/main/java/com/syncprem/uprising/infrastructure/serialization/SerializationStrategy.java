/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

public interface SerializationStrategy extends FileSerializationStrategy,
		BytesSerializationStrategy, StringSerializationStrategy,
		ByteStreamSerializationStrategy, CharStreamSerializationStrategy
{
}
/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

public interface SerializationStrategy extends FileSerializationStrategy,
		BytesSerializationStrategy, StringSerializationStrategy,
		ByteStreamSerializationStrategy, CharStreamSerializationStrategy
{
}
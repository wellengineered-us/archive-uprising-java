/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

public interface KeyValueView
{
	Payload getKeyPayload();

	Schema getKeySchema();

	Payload getOriginalPayload();

	Schema getOriginalSchema();

	Payload getValuePayload();

	Schema getValueSchema();
}

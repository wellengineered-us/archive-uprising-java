/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.streamingio.primitives.Payload;

public interface TextualStreamingRecord extends Payload
{
	long getCharacterNumber();

	long getLineNumber();

	long getRecordIndex();
}

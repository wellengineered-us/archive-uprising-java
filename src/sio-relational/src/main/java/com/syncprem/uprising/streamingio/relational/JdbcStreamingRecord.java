/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.streamingio.primitives.Payload;

public interface JdbcStreamingRecord extends Payload
{
	long getRecordIndex();

	long getResultIndex();
}

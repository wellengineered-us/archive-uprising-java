/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;

public interface JdbcStreamingResult
{
	LifecycleIterator<JdbcStreamingRecord> getRecords();

	int getRecordsAffected();

	long getResultIndex();
}

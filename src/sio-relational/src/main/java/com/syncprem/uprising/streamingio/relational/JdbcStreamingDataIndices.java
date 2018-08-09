/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

public interface JdbcStreamingDataIndices
{
	long getRecordIndex();

	long getResultIndex();
}

/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.util.List;

public interface JdbcStreamingDataReader extends Creatable, Disposable, JdbcStreamingDataRecord, JdbcStreamingDataIndices
{
	List<JdbcStreamingColumn> getColumnSchema() throws Exception;

	default int getDepth() throws Exception
	{
		return 0;
	}

	List<JdbcStreamingParameter> getParameterSchema() throws Exception;

	int getRecordsAffected() throws Exception;

	default boolean isClosed() throws Exception
	{
		return this.isDisposed();
	}

	default void close() throws Exception
	{
		this.dispose();
	}

	boolean nextResult() throws Exception;

	boolean readRecord() throws Exception; /* read() */
}

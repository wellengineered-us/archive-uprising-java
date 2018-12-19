/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

public interface TableSpec
{
	Iterable<ColumnSpec<?>> getColumnSpecs();

	void setColumnSpecs(Iterable<ColumnSpec<?>> columnSpecs);
}

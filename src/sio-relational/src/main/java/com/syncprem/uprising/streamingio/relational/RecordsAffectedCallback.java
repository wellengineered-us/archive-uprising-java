/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

@FunctionalInterface
public interface RecordsAffectedCallback
{
	void invoke(int recordsAffected);
}

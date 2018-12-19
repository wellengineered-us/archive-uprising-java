/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational.uow;

import com.syncprem.uprising.streamingio.relational.IsolationLevel;

public interface UnitOfWorkFactory
{
	UnitOfWork getUnitOfWork(boolean transactional, IsolationLevel isolationLevel) throws Exception;
}

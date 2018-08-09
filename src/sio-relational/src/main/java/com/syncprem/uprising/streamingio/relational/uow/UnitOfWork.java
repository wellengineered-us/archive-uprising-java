/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational.uow;

import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.sql.Connection;
import java.sql.Savepoint;

public interface UnitOfWork extends Disposable
{
	Connection getConnection();

	Disposable getContext();

	void setContext(Disposable context);

	Savepoint getSavepoint();

	boolean isCompleted();

	boolean isDiverged();

	void complete();

	void divergent();
}

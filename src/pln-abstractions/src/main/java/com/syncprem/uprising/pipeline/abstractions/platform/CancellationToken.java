/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.platform;

import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.infrastructure.polyfills.OperationCanceledException;

public interface CancellationToken extends Disposable
{
	boolean isCancellationRequested();

	boolean canBeCanceled();

	void cancel();

	default void throwIfCancellationRequested()
	{
		if (this.isCancellationRequested())
			throw new OperationCanceledException();
	}
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public interface ContextFactory
{
	default Context cloneContext(Context context) throws SyncPremException
	{
		return context;
	}

	Context createContext() throws SyncPremException;
}

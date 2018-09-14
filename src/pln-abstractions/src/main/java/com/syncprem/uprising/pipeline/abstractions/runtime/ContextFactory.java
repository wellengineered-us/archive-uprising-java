/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

public interface ContextFactory
{
	default Context cloneContext(Context context)
	{
		return context;
	}

	Context createContext();
}

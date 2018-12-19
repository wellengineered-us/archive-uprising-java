/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.hostcli;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

import static com.syncprem.uprising.pipeline.core.runtime.HostUtils.bootstrapHost;

public class Program
{
	public static void main(String[] args) throws Exception
	{
		if (args == null)
			throw new ArgumentNullException("args");

		if (args.length != 1 || Utils.isNullOrEmptyString(args[0]))
		{
			System.out.println("USAGE: ... host-config-[file]-path");
			System.exit(-1);
			return;
		}

		final String path = args[0];

		// simply delegate to static bootstrapping method...
		bootstrapHost(path);
	}
}

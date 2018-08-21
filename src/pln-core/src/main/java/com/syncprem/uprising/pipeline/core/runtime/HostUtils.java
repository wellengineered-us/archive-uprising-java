/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.infrastructure.serialization.JsonSerializationStrategyImpl;
import com.syncprem.uprising.pipeline.abstractions.configuration.HostConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Host;

import java.io.File;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__check;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class HostUtils
{
	public static void bootstrapHost(String sourceFilePath) throws Exception
	{
		Host host;
		HostConfiguration hostConfiguration;
		Iterable<Message> messages;

		if (sourceFilePath == null)
			throw new ArgumentNullException("sourceFilePath");

		sourceFilePath = new File(sourceFilePath).getAbsolutePath();

		hostConfiguration = new JsonSerializationStrategyImpl().deserializeObjectFromFile(HostConfiguration.class, sourceFilePath);

		messages = hostConfiguration.validate("Host");

		if (messages != null)
		{
			int count = 0;
			for (Message message : messages)
			{
				if (message == null)
					continue;

				System.out.println(String.format("%s[#%s] => %s", message.getSeverity(), (count + 1), message.getDescription()));

				count++;
			}

			if (count > 0)
				throw new InvalidOperationException(String.format("Host configuration validation failed with error count: %s", count));
		}

		host = Utils.newObjectFromClass(hostConfiguration.getHostClass());

		failFastOnlyWhen(host == null, "host == null");

		try (host)
		{
			host.setConfiguration(hostConfiguration);
			host.create();
			host.run();
		}

		__check();
	}
}

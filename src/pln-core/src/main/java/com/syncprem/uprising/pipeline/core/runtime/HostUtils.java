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

import javax.swing.*;
import java.io.File;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__check;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class HostUtils
{
	public static void bootstrapHost(String sourcePath) throws Exception
	{
		final boolean ENABLE_GUI = true;
		Host host;
		HostConfiguration hostConfiguration;
		Iterable<Message> messages;

		if (sourcePath == null)
			throw new ArgumentNullException("sourcePath");

		final File _sourcePath = new File(sourcePath);

		sourcePath = _sourcePath.getAbsolutePath();

		if (ENABLE_GUI && _sourcePath.isDirectory())
		{
			final JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open host configuration file...");
			chooser.setCurrentDirectory(_sourcePath);
			final int result = chooser.showOpenDialog(null);

			if(result != JFileChooser.APPROVE_OPTION)
				return;

			sourcePath = chooser.getSelectedFile().getAbsolutePath();
		}

		System.out.println(sourcePath);

		hostConfiguration = new JsonSerializationStrategyImpl().deserializeObjectFromFile(HostConfiguration.class, sourcePath);

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

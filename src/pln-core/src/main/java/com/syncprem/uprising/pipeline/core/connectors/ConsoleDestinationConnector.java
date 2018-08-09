/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.destination.AbstractDestinationConnector;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class ConsoleDestinationConnector extends AbstractDestinationConnector<StageSpecificConfiguration>
{
	public ConsoleDestinationConnector()
	{
	}

	private static final BufferedReader textReader = new BufferedReader(new InputStreamReader(System.in));
	private static final PrintStream textWriter = System.out;

	private static BufferedReader getTextReader()
	{
		return textReader;
	}

	private static PrintStream getTextWriter()
	{
		return textWriter;
	}

	@Override
	protected void consumeInternal(Context context, RecordConfiguration configuration, Channel channel) throws Exception
	{
		LifecycleIterator<Record> records;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		records = channel.getRecords();

		failFastOnlyWhen(records == null, "records == null");

		long recordIndex = -1;
		while (records.hasNext())
		{
			final Record record = records.next();

			// OK if record is null
			getTextWriter().println(String.format("[%s] %s", ++recordIndex, record));
		}

		// force full enumeration ONLY to force dispose
		final long recordCount = WrappedIteratorExtensions.forceIteration(records);

		failFastOnlyWhen(recordCount != 0L, "recordCount != 0L");
		failFastOnlyWhen(!records.isDisposed(), "!records.isDisposed()");
	}

	@Override
	protected Class<? extends StageSpecificConfiguration> getStageSpecificConfigurationClass(Object reserved)
	{
		return StageSpecificConfiguration.class;
	}

	@Override
	protected void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");
	}
}

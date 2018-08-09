/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.DelayedProjectionIterator;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.AbstractSourceConnector;
import com.syncprem.uprising.pipeline.core.runtime.RecordImpl;
import com.syncprem.uprising.streamingio.primitives.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class ConsoleSourceConnector extends AbstractSourceConnector<StageSpecificConfiguration>
{
	public ConsoleSourceConnector()
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

	private static Iterator<Payload> getYieldViaConsole(Schema schema)
	{
		List<Payload> payloads;
		Payload payload;

		long recordIndex;
		String line;
		String[] fieldValues;
		Field[] fields;

		if (schema == null)
			throw new ArgumentNullException("schema");

		payloads = new ArrayList<>();
		fields = new Field[schema.getFields().size()];
		schema.getFields().values().toArray(fields);

		recordIndex = 0;
		while (true)
		{
			try
			{
				line = getTextReader().readLine();
			}
			catch (IOException ioex)
			{
				line = null;
			}

			if (Utils.isNullOrEmptyString(line))
				break;

			fieldValues = line.split("\\|");

			payload = new PayloadImpl();

			for (long fieldIndex = 0; fieldIndex < Math.min(fieldValues.length, fields.length); fieldIndex++)
			{
				final Field field = fields[(int) fieldIndex];

				if (field == null)
					continue;

				payload.put(field.getFieldName(), fieldValues[(int) fieldIndex]);
			}

			recordIndex++;

			payloads.add(payload);
		}

		return payloads.iterator();
	}

	@Override
	protected Class<StageSpecificConfiguration> getStageSpecificConfigurationClass(Object reserved)
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
		SchemaBuilder schemaBuilder;
		Schema schema;

		String line;
		String[] fieldNames;

		Map<String, Object> componentState;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		schemaBuilder = SchemaBuilderImpl.create();

		getTextWriter().println("Enter list of schema field names separated by pipe character: ");

		try
		{
			line = getTextReader().readLine();
		}
		catch (IOException ioex)
		{
			line = null;
		}

		if (!Utils.isNullOrEmptyString(line))
		{
			fieldNames = line.split("\\|");

			if (fieldNames == null || fieldNames.length <= 0)
			{
				getTextWriter().println("List of schema field names was invalid; using default (blank).");
				schemaBuilder.addField(Utils.EMPTY_STRING, String.class, false, true, null);
			}
			else
			{
				for (long fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++)
				{
					final String fieldName = fieldNames[(int) fieldIndex];

					if ((Utils.toStringSafe(fieldName)).trim() == Utils.EMPTY_STRING)
						continue;

					schemaBuilder.addField(fieldName, String.class, false, true, null);
				}

				getTextWriter().println(String.format("Building KEY schema: '%s'", String.join(" | ", fieldNames)));
			}
		}

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new HashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		schema = schemaBuilder.build();

		if (schema == null)
			throw new SyncPremException("schema");

		componentState.put(CONTEXT_COMPONENT_SCOPED_SCHEMA, schema);
	}

	@Override
	protected Channel produceInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		Channel channel;
		Schema schema;

		Iterator<Payload> payloads;
		Map<String, Object> componentState;

		LifecycleIterator<Record> records;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new HashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		schema = (Schema) componentState.getOrDefault(CONTEXT_COMPONENT_SCOPED_SCHEMA, null);

		failFastOnlyWhen(schema == null, "schema == null");

		payloads = getYieldViaConsole(schema);

		failFastOnlyWhen(payloads == null, "payloads == null");

		records = new DelayedProjectionIterator<>(payloads, (i, p) -> new RecordImpl(schema, p, Utils.EMPTY_STRING, null, null));

		failFastOnlyWhen(records == null, "records == null");

		channel = context.createChannel(records);

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

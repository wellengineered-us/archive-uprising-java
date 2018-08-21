/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.*;
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

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__enter;
import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__leave;
import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__watching;
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

	public LifecycleIterator<Payload> getYieldViaConsole(Schema schema)
	{
		LifecycleIterator<Payload> iterator;

		UUID __ = __enter();

		Field[] fields;

		if (schema == null)
			throw new ArgumentNullException("schema");

		failFastOnlyWhen(schema.getFields() == null, "schema.getFields() == null");

		final int fieldCount = schema.getFields().size();

		fields = new Field[fieldCount];
		schema.getFields().values().toArray(fields);

		iterator = new AbstractYieldIterator<Payload>()
		{
			private long recordIndex;
			final long recordInitial = 0L;

			@Override
			protected void create(boolean creating) throws Exception
			{
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
			}

			@Override
			protected Iterator<Payload> newIterator(int state)
			{
				// do nothing
				return this;
			}

			@Override
			protected boolean onTryYield(TryOut<Payload> value) throws Exception
			{
				if (value == null)
					throw new ArgumentNullException("value");

				if (this.recordIndex > -1L)
				{
					String line;
					String[] fieldValues;
					final Payload payload = new PayloadImpl(fieldCount);

					try
					{
						line = getTextReader().readLine();
					}
					catch (IOException ioex)
					{
						line = null;
					}

					if (Utils.isNullOrEmptyString(line))
						return false;

					fieldValues = line.split("\\|");

					for (long fieldIndex = 0; fieldIndex < Math.min(fieldValues.length, fields.length); fieldIndex++)
					{
						final Field field = fields[(int) fieldIndex];

						if (field == null)
							continue;

						payload.put(field.getFieldName(), fieldValues[(int) fieldIndex]);
					}

					value.setValue(payload);
					return true;
				}

				return false;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{
				this.recordIndex = -1; // }
			}

			@Override
			protected void onYieldFault(Exception ex)
			{
			}

			@Override
			protected void onYieldResume() throws Exception
			{
			}

			@Override
			protected void onYieldReturn(Payload value) throws Exception
			{
				this.recordIndex++; // for(..., ..., value++)
			}

			@Override
			protected void onYieldStart() throws Exception
			{
				this.recordIndex = recordInitial; // for(int value = lb; ...
			}
		};

		__watching(__, iterator);
		__leave(__);

		return iterator;
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

		failFastOnlyWhen(componentState == null, "componentState == null");

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

		failFastOnlyWhen(componentState == null, "componentState == null");

		schema = (Schema) componentState.getOrDefault(CONTEXT_COMPONENT_SCOPED_SCHEMA, null);

		failFastOnlyWhen(schema == null, "schema == null");

		payloads = getYieldViaConsole(schema);

		failFastOnlyWhen(payloads == null, "payloads == null");

		records = new DelayedProjectionIterator<>(payloads, (i, p) -> new RecordImpl(schema, p, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE));

		failFastOnlyWhen(records == null, "records == null");

		channel = context.createChannel(records);

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

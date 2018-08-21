/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.AbstractSourceConnector;
import com.syncprem.uprising.pipeline.core.configurations.NullConnectorSpecificConfiguration;
import com.syncprem.uprising.pipeline.core.runtime.RecordImpl;
import com.syncprem.uprising.streamingio.primitives.*;
import com.syncprem.uprising.streamingio.textual.TextualStreamingRecord;

import java.util.*;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__enter;
import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__leave;
import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.__watching;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class NullSourceConnector extends AbstractSourceConnector<NullConnectorSpecificConfiguration>
{
	public NullSourceConnector()
	{
	}

	private static final Random random = new Random();

	private static Random getRandom()
	{
		return random;
	}

	private Schema getSchema()
	{
		SchemaBuilder schemaBuilder;

		schemaBuilder = SchemaBuilderImpl.create();

		schemaBuilder.addField(Utils.EMPTY_STRING, UUID.class, false, true, null);

		for (long fieldIndex = 0; fieldIndex < (long)this.getSpecification().getMaxRandomFieldCount(); fieldIndex++)
		{
			final String fieldName = String.format(this.getSpecification().getFieldNameFormat(), fieldIndex);

			schemaBuilder.addField(fieldName, Double.class, false, false, null);
		}

		return schemaBuilder.build();
	}

	public LifecycleIterator<Payload> getRandomPayloads(Schema schema)
	{
		LifecycleIterator<Payload> iterator;

		UUID __ = __enter();

		Field[] fields;

		if (schema == null)
			throw new ArgumentNullException("schema");

		failFastOnlyWhen(schema.getFields() == null, "schema.getFields() == null");

		final int fieldCount = schema.getFields().size();
		final long recordCount = getRandom().nextInt((int)(long)this.getSpecification().getMaxRandomRecordCount());

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

				if (this.recordIndex < recordCount)
				{
					final Payload payload = new PayloadImpl(fieldCount);

					for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++)
					{
						final Field field = fields[fieldIndex];

						if (field == null)
							continue;

						if (field.isFieldKeyComponent())
							payload.put(field.getFieldName(), UUID.randomUUID());
						else
							payload.put(field.getFieldName(), getRandom().nextDouble());
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
	protected Class<NullConnectorSpecificConfiguration> getStageSpecificConfigurationClass(Object reserved)
	{
		return NullConnectorSpecificConfiguration.class;
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
		Schema schema;
		Map<String, Object> componentState;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new HashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		failFastOnlyWhen(componentState == null, "componentState == null");

		schema = getSchema();

		failFastOnlyWhen(schema == null, "schema == null");

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

		payloads = getRandomPayloads(schema);

		failFastOnlyWhen(payloads == null, "payloads == null");

		records = new DelayedProjectionIterator<>(payloads, (i, p) -> new RecordImpl(schema, p, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE));

		failFastOnlyWhen(records == null, "records == null");

		channel = context.createChannel(records);

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

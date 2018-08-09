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

import java.util.*;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class NullSourceConnector extends AbstractSourceConnector<StageSpecificConfiguration>
{
	public NullSourceConnector()
	{
	}

	private static final int FIELD_COUNT = 5;
	private static final String FIELD_NAME = "RandomValue_%s";
	private static final int MAX_RECORDS = 100;
	private static final Random random = new Random();

	private static Random getRandom()
	{
		return random;
	}

	private static Schema getSchema()
	{
		SchemaBuilder schemaBuilder;

		schemaBuilder = SchemaBuilderImpl.create();

		schemaBuilder.addField(Utils.EMPTY_STRING, UUID.class, false, true, null);

		for (long fieldIndex = 0; fieldIndex < FIELD_COUNT; fieldIndex++)
		{
			final String fieldName = String.format(FIELD_NAME, fieldIndex);

			schemaBuilder.addField(fieldName, Double.class, false, false, null);
		}

		return schemaBuilder.build();
	}

	private static Iterator<Payload> getRandomPayloads(Schema schema)
	{
		Payload payload;
		Field[] fields;
		List<Payload> payloads;

		long recordCount;

		if (schema == null)
			throw new ArgumentNullException("schema");

		fields = new Field[schema.getFields().size()];
		schema.getFields().values().toArray(fields);
		recordCount = getRandom().nextInt(MAX_RECORDS);

		payloads = new ArrayList<>();
		for (long recordIndex = 0; recordIndex < recordCount; recordIndex++)
		{
			payload = new PayloadImpl();

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

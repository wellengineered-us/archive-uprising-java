/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.AbstractSourceConnector;
import com.syncprem.uprising.pipeline.core.configurations.JdbcConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.primitives.*;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingColumnImpl;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingParameter;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingRecord;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingResult;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWork;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkExtensions;

import java.util.*;
import java.util.stream.StreamSupport;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class JdbcSourceConnector extends AbstractSourceConnector<JdbcConnectorSpecificConfiguration>
{
	public JdbcSourceConnector()
	{
	}

	private UnitOfWork sourceUnitOfWork;

	private UnitOfWork getSourceUnitOfWork()
	{
		return this.sourceUnitOfWork;
	}

	private void setSourceUnitOfWork(UnitOfWork sourceUnitOfWork)
	{
		this.sourceUnitOfWork = sourceUnitOfWork;
	}

	@Override
	protected Class<JdbcConnectorSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return JdbcConnectorSpecificConfiguration.class;
	}

	private LifecycleIterator<Channel> getMultiplexedChannels(Context context, Iterator<JdbcStreamingResult> results)
	{
		LifecycleIterator<Channel> channels;
		Schema schema;
		Map<String, Object> componentState;

		if (context == null)
			throw new ArgumentNullException("context");

		if (results == null)
			throw new ArgumentNullException("results");

		failFastOnlyWhen(context == null, "context == null");

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(JdbcSourceConnector.this, (componentState = new LinkedHashMap<>()));
		else
			componentState = context.getLocalState().get(JdbcSourceConnector.this);

		failFastOnlyWhen(componentState == null, "componentState == null");

		schema = (Schema) componentState.getOrDefault(CONTEXT_COMPONENT_SCOPED_SCHEMA, null);

		failFastOnlyWhen(schema == null, "schema == null");

		channels = new AbstractForEachYieldIterator<JdbcStreamingResult, Channel>(results, (index, result) ->
		{
			Channel channel;
			Iterator<JdbcStreamingRecord> payloads;
			LifecycleIterator<Record> records;
			Map<String, Field> fields;

			failFastOnlyWhen(schema == null, "schema == null");

			fields = schema.getFields();

			failFastOnlyWhen(fields == null, "fields == null");

			failFastOnlyWhen(index >= fields.size() || fields.size() == 0, "index >= fields.size() || fields.size() == 0");

			final Field[] values = new Field[fields.size()];
			fields.values().toArray(values);

			failFastOnlyWhen(values == null, "values == null");

			final Field field = values[(int) index];

			failFastOnlyWhen(field == null, "field == null");

			final Schema fieldSchema = field.getFieldSchema();

			failFastOnlyWhen(fieldSchema == null, "fieldSchema == null");


			failFastOnlyWhen(result == null, "result == null");

			payloads = result.getRecords();

			failFastOnlyWhen(payloads == null, "payloads == null");

			records = new DelayedProjectionIterator<>(payloads, (i, p) -> context.createRecord(fieldSchema, p, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE));

			failFastOnlyWhen(records == null, "records == null");

			channel = context.createChannel(records);
			//channel = new ChannelImpl(records);

			failFastOnlyWhen(channel == null, "channel == null");

			return channel; // yield return ;)
		})
		{
		};

		return channels;
	}

	@Override
	protected void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		Iterator<JdbcStreamingResult> results;

		Iterable<JdbcStreamingParameter> parameters;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		failFastOnlyWhen(this.getSourceUnitOfWork() == null, "this.getSourceUnitOfWork() == null");

		if (this.getSpecification().getPostExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getPostExecuteCommand().getCommandText()))
		{
			parameters = this.getSpecification().getPostExecuteCommand().getParameters(this.getSourceUnitOfWork());

			results = UnitOfWorkExtensions.executeResults(this.getSourceUnitOfWork(),
					this.getSpecification().getPostExecuteCommand().getCommandType(),
					this.getSpecification().getPostExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			WrappedIteratorExtensions.forceIteration(results); // force execution
		}

		if (this.getSourceUnitOfWork() != null)
		{
			this.getSourceUnitOfWork().complete();
			this.getSourceUnitOfWork().dispose();
			this.setSourceUnitOfWork(null);
		}
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		SchemaBuilder rootSchemaBuilder, childSchemaBuilder;
		Schema rootSchema, childSchema;
		Iterator<JdbcStreamingResult> results;
		Iterator<JdbcStreamingRecord> records;

		Map<String, Object> componentState;

		UnitOfWork sourceUnitOfWork;
		Iterable<JdbcStreamingParameter> parameters;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		sourceUnitOfWork = this.getSpecification().getUnitOfWork();

		failFastOnlyWhen(sourceUnitOfWork == null, "sourceUnitOfWork == null");

		this.setSourceUnitOfWork(sourceUnitOfWork);

		if (this.getSpecification().getPreExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getPreExecuteCommand().getCommandText()))
		{
			parameters = this.getSpecification().getPreExecuteCommand().getParameters(this.getSourceUnitOfWork());

			results = UnitOfWorkExtensions.executeResults(this.getSourceUnitOfWork(),
					this.getSpecification().getPreExecuteCommand().getCommandType(),
					this.getSpecification().getPreExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			WrappedIteratorExtensions.forceIteration(results, (index, item) -> WrappedIteratorExtensions.forceIteration(item.getRecords())); // force execution
		}

		// execute schema only
		if (this.getSpecification().getExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getExecuteCommand().getCommandText()))
		{
			parameters = this.getSpecification().getExecuteCommand().getParameters(this.getSourceUnitOfWork());

			results = UnitOfWorkExtensions.executeSchemaResults(this.getSourceUnitOfWork(),
					this.getSpecification().getExecuteCommand().getCommandType(),
					this.getSpecification().getExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			if (!context.getLocalState().containsKey(this))
				context.getLocalState().put(this, (componentState = new LinkedHashMap<>()));
			else
				componentState = context.getLocalState().get(this);

			failFastOnlyWhen(componentState == null, "componentState == null");

			rootSchemaBuilder = SchemaBuilderImpl.create().withType(SchemaType.ARRAY);

			while (results.hasNext())
			{
				final JdbcStreamingResult result = results.next();

				failFastOnlyWhen(result == null, "result == null");

				childSchemaBuilder = SchemaBuilderImpl.create();

				failFastOnlyWhen(childSchemaBuilder == null, "childSchemaBuilder == null");

				records = result.getRecords();

				failFastOnlyWhen(records == null, "records == null");

				while (records.hasNext())
				{
					final JdbcStreamingRecord record = records.next();

					failFastOnlyWhen(record == null, "record == null");

					final String fieldName = (String) record.getOrDefault(JdbcStreamingColumnImpl.COLUMN_NAME, null);
					final Class<?> fieldClass = (Class<?>) record.getOrDefault(JdbcStreamingColumnImpl.DATA_CLASS, null);
					final boolean isFieldOptional = (Boolean) record.getOrDefault(JdbcStreamingColumnImpl.IS_NULLABLE, false);
					final boolean isKeyComponent = (Boolean) record.getOrDefault(JdbcStreamingColumnImpl.IS_KEY, false);

					// TODO: ensure nullable primitive types
					childSchemaBuilder.addField(fieldName, fieldClass, isFieldOptional, isKeyComponent, null);
				}

				childSchema = childSchemaBuilder.build();

				failFastOnlyWhen(childSchema == null, "childSchema == null");

				rootSchemaBuilder.addField(UUID.randomUUID().toString(), Object.class, false, false, childSchema);
			}

			rootSchema = rootSchemaBuilder.build();

			failFastOnlyWhen(rootSchema == null, "rootSchema == null");

			componentState.put(CONTEXT_COMPONENT_SCOPED_SCHEMA, rootSchema);
		}
	}

	@Override
	protected Channel produceInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		LifecycleIterator<JdbcStreamingResult> results;
		Iterable<JdbcStreamingParameter> parameters;

		LifecycleIterator<Channel> channels;
		LifecycleIterator<Record> records;
		Channel channel;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (this.getSpecification().getExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getExecuteCommand().getCommandText()))
		{
			parameters = this.getSpecification().getExecuteCommand().getParameters(this.getSourceUnitOfWork());

			results = UnitOfWorkExtensions.executeResults(this.getSourceUnitOfWork(),
					this.getSpecification().getExecuteCommand().getCommandType(),
					this.getSpecification().getExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			channels = this.getMultiplexedChannels(context, results);

			final Iterator<Record> records_ = StreamSupport.stream(Spliterators.spliteratorUnknownSize(channels, Spliterator.ORDERED), false)
					.flatMap(
							c -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(c.getRecords(), Spliterator.ORDERED), false)
					).iterator();

			failFastOnlyWhen(records_ == null, "records_ == null");

			records = WrappedIteratorExtensions.toLifecycleIterator(records_);

			failFastOnlyWhen(records == null, "records == null");

			channel = context.createChannel(records);
		}
		else
			channel = context.createEmptyChannel();

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

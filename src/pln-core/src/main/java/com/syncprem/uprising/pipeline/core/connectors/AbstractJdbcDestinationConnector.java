/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.destination.AbstractDestinationConnector;
import com.syncprem.uprising.pipeline.core.configurations.JdbcConnectorSpecificConfiguration;
import com.syncprem.uprising.pipeline.core.connectors.internal.RecordAdaptingDataReaderImpl;
import com.syncprem.uprising.streamingio.primitives.Schema;
import com.syncprem.uprising.streamingio.primitives.SchemaBuilder;
import com.syncprem.uprising.streamingio.primitives.SchemaBuilderImpl;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;
import com.syncprem.uprising.streamingio.relational.*;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWork;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkExtensions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractJdbcDestinationConnector extends AbstractDestinationConnector<JdbcConnectorSpecificConfiguration>
{
	protected AbstractJdbcDestinationConnector()
	{
	}

	private UnitOfWork destinationUnitOfWork;

	protected UnitOfWork getDestinationUnitOfWork()
	{
		return this.destinationUnitOfWork;
	}

	private void setDestinationUnitOfWork(UnitOfWork sourceUnitOfWork)
	{
		this.destinationUnitOfWork = sourceUnitOfWork;
	}

	@Override
	protected final void consumeInternal(Context context, RecordConfiguration configuration, Channel channel) throws Exception
	{
		LifecycleIterator<Record> records;
		JdbcStreamingDataReader dataReader;

		Schema schema;
		Map<String, Object> componentState;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new LinkedHashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		failFastOnlyWhen(componentState == null, "componentState == null");

		schema = (Schema) componentState.getOrDefault(CONTEXT_COMPONENT_SCOPED_SCHEMA, null);

		failFastOnlyWhen(schema == null, "schema == null");

		records = channel.getRecords();

		failFastOnlyWhen(records == null, "records	 == null");

		dataReader = RecordAdaptingDataReaderImpl.create(records);

		try
		{
			this.consumeInternal(context, configuration, dataReader);
		}
		finally
		{
			// just in case consumeInternal(3) did not or could not enumerate to completion for disposal...
			if (dataReader != null)
			{
				do
				{
					while (dataReader.readRecord())
					{
					}
				}
				while (dataReader.nextResult());

				dataReader.dispose();
			}
		}

		// force full enumeration ONLY to force dispose
		final long recordCount = WrappedIteratorExtensions.forceIteration(records);

		failFastOnlyWhen(recordCount != 0L, "recordCount != 0L");
		failFastOnlyWhen(!records.isDisposed(), "!records.isDisposed()");
	}

	protected abstract void consumeInternal(Context context, RecordConfiguration configuration, JdbcStreamingDataReader dataReader) throws Exception;

	@Override
	protected Class<JdbcConnectorSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return JdbcConnectorSpecificConfiguration.class;
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

		failFastOnlyWhen(this.getDestinationUnitOfWork() == null, "this.getDestinationUnitOfWork() == null");

		if (this.getSpecification().getPostExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getPostExecuteCommand().getCommandText()))
		{
			parameters = this.getSpecification().getPostExecuteCommand().getParameters(this.getDestinationUnitOfWork());

			results = UnitOfWorkExtensions.executeResults(this.getDestinationUnitOfWork(),
					this.getSpecification().getPostExecuteCommand().getCommandType(),
					this.getSpecification().getPostExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			WrappedIteratorExtensions.forceIteration(results, (index, item) -> WrappedIteratorExtensions.forceIteration(item.getRecords())); // force execution
		}

		if (this.getDestinationUnitOfWork() != null)
		{
			this.getDestinationUnitOfWork().complete();
			this.getDestinationUnitOfWork().dispose();
			this.setDestinationUnitOfWork(null);
		}
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		SchemaBuilder schemaBuilder;
		Schema schema;

		Iterator<JdbcStreamingResult> results;
		Iterator<JdbcStreamingRecord> records;

		Map<String, Object> componentState;

		UnitOfWork destinationUnitOfWork;
		Iterable<JdbcStreamingParameter> parameters;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		destinationUnitOfWork = this.getSpecification().getUnitOfWork(false, IsolationLevel.NONE);

		failFastOnlyWhen(destinationUnitOfWork == null, "destinationUnitOfWork == null");

		this.setDestinationUnitOfWork(destinationUnitOfWork);

		if (this.getSpecification().getPreExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getPreExecuteCommand().getCommandText()))
		{
			parameters = this.getSpecification().getPreExecuteCommand().getParameters(this.getDestinationUnitOfWork());

			results = UnitOfWorkExtensions.executeResults(this.getDestinationUnitOfWork(),
					this.getSpecification().getPreExecuteCommand().getCommandType(),
					this.getSpecification().getPreExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			WrappedIteratorExtensions.forceIteration(results, (index, item) -> WrappedIteratorExtensions.forceIteration(item.getRecords())); // force execution
		}

		// execute schema only
		if (this.getSpecification().getExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getExecuteCommand().getCommandText()))
		{
			JdbcStreamingResult singlgResult = null;

			parameters = this.getSpecification().getExecuteCommand().getParameters(this.getDestinationUnitOfWork());

			// single result expected ???
			results = UnitOfWorkExtensions.executeSchemaResults(this.getDestinationUnitOfWork(),
					this.getSpecification().getExecuteCommand().getCommandType(),
					this.getSpecification().getExecuteCommand().getCommandText(), parameters);

			failFastOnlyWhen(results == null, "results == null");

			if (results.hasNext())
				singlgResult = results.next();

			failFastOnlyWhen(singlgResult == null, "singlgResult == null");

			records = singlgResult.getRecords();

			failFastOnlyWhen(records == null, "records == null");

			if (!context.getLocalState().containsKey(this))
				context.getLocalState().put(this, (componentState = new LinkedHashMap<>()));
			else
				componentState = context.getLocalState().get(this);

			failFastOnlyWhen(componentState == null, "componentState == null");

			schemaBuilder = SchemaBuilderImpl.create();

			while (records.hasNext())
			{
				final JdbcStreamingRecord record = records.next();

				failFastOnlyWhen(record == null, "record == null");

				final String fieldName = (String) record.getOrDefault(JdbcStreamingColumnImpl.COLUMN_NAME, null);
				final Class<?> fieldClass = (Class<?>) record.getOrDefault(JdbcStreamingColumnImpl.DATA_CLASS, null);
				final boolean isFieldOptional = (Boolean) record.getOrDefault(JdbcStreamingColumnImpl.IS_NULLABLE, false);
				final boolean isKeyComponent = (Boolean) record.getOrDefault(JdbcStreamingColumnImpl.IS_KEY, false);

				// TODO: ensure nullable primitive types
				schemaBuilder.addField(fieldName, fieldClass, isFieldOptional, isKeyComponent, null);
			}

			schema = schemaBuilder.build();

			failFastOnlyWhen(schema == null, "schema == null");

			componentState.put(CONTEXT_COMPONENT_SCOPED_SCHEMA, schema);
		}
	}
}

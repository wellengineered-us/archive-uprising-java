/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.DelayedProjectionIterator;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.AbstractSourceConnector;
import com.syncprem.uprising.pipeline.core.configurations.AbstractTextualConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.AbstractTextualFieldConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.AbstractTextualFileConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.primitives.*;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;
import com.syncprem.uprising.streamingio.textual.TextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.TextualReader;
import com.syncprem.uprising.streamingio.textual.TextualSpec;
import com.syncprem.uprising.streamingio.textual.TextualStreamingRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractTextualSourceConnector
		<
				TTextualFieldConfiguration extends AbstractTextualFieldConfiguration,
				TTextualConfiguration extends AbstractTextualConfiguration<TTextualFieldConfiguration, TTextualFieldSpec, TTextualSpec>,
				TTextualFieldSpec extends TextualFieldSpec,
				TTextualSpec extends TextualSpec<TTextualFieldSpec>,
				TTextualConnectorSpecificConfiguration extends AbstractTextualFileConnectorSpecificConfiguration<TTextualFieldSpec, TTextualSpec, TTextualFieldConfiguration, TTextualConfiguration>,
				TTextualReader extends TextualReader<TTextualFieldSpec, TTextualSpec>
				>
		extends AbstractSourceConnector<TTextualConnectorSpecificConfiguration>
{
	protected AbstractTextualSourceConnector()
	{
	}

	private TTextualReader textualReader;

	public TTextualReader getTextualReader()
	{
		return this.textualReader;
	}

	private void setTextualReader(TTextualReader textualReader)
	{
		this.textualReader = textualReader;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		super.create(creating);

		if (creating)
		{
			// do nothing
		}
	}

	protected abstract TTextualReader createTextualReader(Reader reader, TTextualSpec textualSpec);

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			if (this.getTextualReader() != null)
			{
				this.getTextualReader().dispose();
				this.setTextualReader(null);
			}
		}

		super.dispose(disposing);
	}

	@Override
	protected void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (this.getTextualReader() != null)
		{
			this.getTextualReader().dispose();
			this.setTextualReader(null);
		}
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		SchemaBuilder schemaBuilder;
		Schema schema;
		Iterator<? extends TTextualFieldSpec> headers;
		TTextualSpec spec;

		TTextualConfiguration textualConfiguration;
		Map<String, Object> componentState;

		TTextualReader textualReader;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		textualConfiguration = this.getSpecification().getTextualConfiguration();

		failFastOnlyWhen(textualConfiguration == null, "textualConfiguration == null");

		spec = textualConfiguration.mapToSpec();

		failFastOnlyWhen(spec == null, "spec == null");

		textualReader = this.createTextualReader(new FileReader(this.getSpecification().getTextualFilePath()), spec);

		failFastOnlyWhen(textualReader == null, "textualReader == null");

		this.setTextualReader(textualReader);

		headers = this.getTextualReader().readHeaderFields();

		failFastOnlyWhen(headers == null, "headers == null");

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new LinkedHashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		failFastOnlyWhen(componentState == null, "componentState == null");

		schemaBuilder = SchemaBuilderImpl.create();

		failFastOnlyWhen(schemaBuilder == null, "schemaBuilder == null");

		WrappedIteratorExtensions.forceIteration(headers);

		failFastOnlyWhen(headers.hasNext(), "headers.hasNext()");

		for (TTextualFieldSpec header : spec.getTextualHeaderSpecs())
		{
			if (header == null)
				continue;

			schemaBuilder.addField(header.getFieldTitle(), header.getFieldType().ToJvmClass(), header.isFieldRequired(), header.isFieldIdentity(), null);
		}

		schema = schemaBuilder.build();

		failFastOnlyWhen(schema == null, "schema == null");

		componentState.put(CONTEXT_COMPONENT_SCOPED_SCHEMA, schema);
	}

	@Override
	protected Channel produceInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		Channel channel;
		Schema schema;

		Iterator<TextualStreamingRecord> payloads;
		Map<String, Object> componentState;

		LifecycleIterator<Record> records;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new LinkedHashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		failFastOnlyWhen(componentState == null, "componentState == null");

		schema = (Schema) componentState.getOrDefault(CONTEXT_COMPONENT_SCOPED_SCHEMA, null);

		failFastOnlyWhen(schema == null, "schema == null");

		payloads = this.getTextualReader().readRecords();

		failFastOnlyWhen(payloads == null, "payloads == null");

		records = new DelayedProjectionIterator<>(payloads, (i, p) -> context.createRecord(schema, p, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE));

		failFastOnlyWhen(records == null, "records == null");

		channel = context.createChannel(records);

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

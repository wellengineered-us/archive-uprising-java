/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.infrastructure.serialization.SerializationStrategy;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.AbstractSourceConnector;
import com.syncprem.uprising.pipeline.core.configurations.WebApiConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.primitives.*;
import com.syncprem.uprising.streamingio.proxywrappers.strategies.CompressionStrategy;
import com.syncprem.uprising.streamingio.restful.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class WebApiSourceConnector extends AbstractSourceConnector<WebApiConnectorSpecificConfiguration>
{
	public WebApiSourceConnector()
	{
	}

	private HttpStreamingClient sourceHttpStreamingClient;

	private HttpStreamingClient getSourceHttpStreamingClient()
	{
		return this.sourceHttpStreamingClient;
	}

	private void setSourceHttpStreamingClient(HttpStreamingClient sourceHttpStreamingClient)
	{
		this.sourceHttpStreamingClient = sourceHttpStreamingClient;
	}

	@Override
	protected Class<? extends WebApiConnectorSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return WebApiConnectorSpecificConfiguration.class;
	}

	private Iterator<Payload> getPayloadUsingHttpResponseContent(Map<String, Iterable<String>> responseHeaders, HttpStreamingContent responseContent)
	{
		if (responseHeaders == null)
			throw new ArgumentNullException("responseHeaders");

		if (responseContent == null)
			throw new ArgumentNullException("responseContent");

		System.out.println(responseHeaders.get("Content-Type"));

		return new AbstractYieldIterator<Payload>()
		{
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
				return null;
			}

			@Override
			protected boolean onTryYield(TryOut<Payload> value) throws Exception
			{
				return false;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{

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

			}

			@Override
			protected void onYieldStart() throws Exception
			{

			}
		};
	}

	private Schema getSchemaUsingHeadResponse(HttpStreamingResponse httpStreamingResponse)
	{
		Schema schema;
		SchemaBuilder schemaBuilder;

		if (httpStreamingResponse == null)
			throw new ArgumentNullException("httpStreamingResponse");

		schemaBuilder = SchemaBuilderImpl.create().withType(SchemaType.UNKNOWN);
		schema = schemaBuilder.build();

		return schema;
	}

	@Override
	protected void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		failFastOnlyWhen(this.getSourceHttpStreamingClient() == null, "this.getSourceHttpStreamingClient() == null");

		if (this.getSourceHttpStreamingClient() != null)
		{
			this.getSourceHttpStreamingClient().dispose();
			this.setSourceHttpStreamingClient(null);
		}
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		Schema schema;
		Map<String, Object> componentState;

		HttpStreamingClient sourceHttpStreamingClient;
		HttpStreamingRequest httpStreamingRequest;
		HttpStreamingResponse httpStreamingResponse = null;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		sourceHttpStreamingClient = new HttpStreamingClientImpl();

		this.setSourceHttpStreamingClient(sourceHttpStreamingClient);

		if (!Utils.isNullOrEmptyString(this.getSpecification().getEndpointUri()))
		{
			final URL uri = new URL(this.getSpecification().getEndpointUri());

			final HttpStreamingContent requestContent = new EmptyStreamContent();

			httpStreamingRequest = new HttpStreamingRequestImpl(Utils.EMPTY_STRING, HttpMethod.HEAD /* force to get headers only */,
					uri, Collections.emptyMap(), this.getSpecification().getHttpScope(), requestContent);

			httpStreamingResponse = this.getSourceHttpStreamingClient().send(httpStreamingRequest);

			failFastOnlyWhen(httpStreamingResponse == null, "httpStreamingResponse == null");

			failFastOnlyWhen(httpStreamingResponse.getResponseStatus() != HttpStatus.HTTP_OK, "httpStreamingResponse.getResponseStatus() != HttpStatus.HTTP_OK");
		}

		if (!context.getLocalState().containsKey(this))
			context.getLocalState().put(this, (componentState = new HashMap<>()));
		else
			componentState = context.getLocalState().get(this);

		failFastOnlyWhen(componentState == null, "componentState == null");

		schema = this.getSchemaUsingHeadResponse(httpStreamingResponse);

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

		if (!Utils.isNullOrEmptyString(this.getSpecification().getEndpointUri()))
		{
			try (HttpStreamingClient httpStreamingClient = new HttpStreamingClientImpl())
			{
				HttpStreamingRequest httpStreamingRequest;
				HttpStreamingResponse httpStreamingResponse;

				final URL uri = new URL(this.getSpecification().getEndpointUri());

				CompressionStrategy compressionStrategy;
				SerializationStrategy serializationStrategy;

				compressionStrategy = Utils.newObjectFromClass(this.getSpecification().getCompressionStrategyClass());

				failFastOnlyWhen(compressionStrategy == null, "compressionStrategy == null");

				serializationStrategy = Utils.newObjectFromClass(this.getSpecification().getSerializationStrategyClass());

				failFastOnlyWhen(serializationStrategy == null, "serializationStrategy == null");

				final HttpStreamingContent requestContent = new AbstractHttpStreamingContent()
				{
					@Override
					protected InputStream createContentInputStream() throws IOException
					{
						return null;
					}

					@Override
					protected void serializeToOutputStream(OutputStream outputStream) throws IOException
					{
						if (outputStream == null)
							throw new ArgumentNullException("outputStream");
					}

					@Override
					protected boolean tryComputeLength(TryOut<Long> value) throws IOException
					{
						if (value == null)
							throw new ArgumentNullException("value");

						return false;
					}
				};

				httpStreamingRequest = new HttpStreamingRequestImpl(Utils.EMPTY_STRING, this.getSpecification().getHttpMethod(),
						uri, Collections.emptyMap(), this.getSpecification().getHttpScope(), requestContent);

				httpStreamingResponse = httpStreamingClient.send(httpStreamingRequest);

				failFastOnlyWhen(httpStreamingResponse == null, "httpStreamingResponse == null");

				failFastOnlyWhen(!httpStreamingResponse.isRestfulCongruentSuccessResult(), "!httpStreamingResponse.isRestfulCongruentSuccessResult()");

				// deserialize response into payload...
				final HttpStreamingContent responseContent = httpStreamingResponse.getResponseContent();

				failFastOnlyWhen(responseContent == null, "responseContent == null");

				payloads = this.getPayloadUsingHttpResponseContent(httpStreamingResponse.getResponseHeaders(), responseContent);

				failFastOnlyWhen(payloads == null, "payloads == null");

				records = new DelayedProjectionIterator<>(payloads, (i, p) -> context.createRecord(schema, p, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE));

				failFastOnlyWhen(records == null, "records == null");

				channel = context.createChannel(records);
			}
		}
		else
			channel = context.createEmptyChannel();

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

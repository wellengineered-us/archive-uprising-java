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
	protected Class<? extends WebApiConnectorSpecificConfiguration> getStageSpecificConfigurationClass(Object reserved)
	{
		return WebApiConnectorSpecificConfiguration.class;
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
		HttpStreamingClient sourceHttpStreamingClient;
		HttpStreamingRequest httpStreamingRequest;
		HttpStreamingResponse httpStreamingResponse;

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
	}

	@Override
	protected Channel produceInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		Channel channel;

		SchemaBuilder rootSchemaBuilder, childSchemaBuilder;
		Schema rootSchema, childSchema;

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

		// TODO: JIT-infer schema based on JSON?
		rootSchemaBuilder = SchemaBuilderImpl.create().withType(SchemaType.UNKNOWN);

		rootSchema = rootSchemaBuilder.build();

		failFastOnlyWhen(rootSchema == null, "rootSchema == null");

		componentState.put(CONTEXT_COMPONENT_SCOPED_SCHEMA, rootSchema); // just for good measure

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

				//httpStreamingResponse.getResponseContent().copyTo(System.out);
			}

			channel = context.createEmptyChannel(); //??
		}
		else
			channel = context.createEmptyChannel();

		failFastOnlyWhen(channel == null, "channel == null");

		return channel;
	}
}

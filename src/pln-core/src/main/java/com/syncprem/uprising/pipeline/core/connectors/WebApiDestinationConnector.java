/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.SerializationStrategy;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.destination.AbstractDestinationConnector;
import com.syncprem.uprising.pipeline.core.configurations.WebApiConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.proxywrappers.ProgressWrappedOutputStreamImpl;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;
import com.syncprem.uprising.streamingio.proxywrappers.strategies.CompressionStrategy;
import com.syncprem.uprising.streamingio.restful.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class WebApiDestinationConnector extends AbstractDestinationConnector<WebApiConnectorSpecificConfiguration>
{
	public WebApiDestinationConnector()
	{
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

				final HttpStreamingContent requestContent = new PushOutputStreamContent(outputStream ->
				{
					if (outputStream == null)
						throw new ArgumentNullException("outputStream");

					try
					{
						// NOTE: this is an I/O push model...
						// e.g. do you "push" content to the network, or does the infrastructure "pull" it from you
						outputStream = compressionStrategy.wrap(outputStream);
						outputStream = new ProgressWrappedOutputStreamImpl(outputStream); // uncompressed
						serializationStrategy.setObjectToStream(outputStream, records);
					}
					catch (Exception ex)
					{
						throw new IOException("Failed to serialize to output stream; see inner exception.", ex);
					}

					return outputStream; // ignored
				});

				httpStreamingRequest = new HttpStreamingRequestImpl(Utils.EMPTY_STRING, this.getSpecification().getHttpMethod(),
						uri, Collections.emptyMap(), this.getSpecification().getHttpScope(), requestContent);

				httpStreamingResponse = httpStreamingClient.send(httpStreamingRequest);

				failFastOnlyWhen(httpStreamingResponse == null, "httpStreamingResponse == null");

				failFastOnlyWhen(!httpStreamingResponse.isRestfulCongruentSuccessResult(), "!httpStreamingResponse.isRestfulCongruentSuccessResult()");

				//httpStreamingResponse.getResponseContent().copyTo(System.out);
			}
		}

		// force full enumeration ONLY to force dispose
		final long recordCount = WrappedIteratorExtensions.forceIteration(records);

		failFastOnlyWhen(recordCount != 0L, "recordCount != 0L");
		failFastOnlyWhen(!records.isDisposed(), "!records.isDisposed()");
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

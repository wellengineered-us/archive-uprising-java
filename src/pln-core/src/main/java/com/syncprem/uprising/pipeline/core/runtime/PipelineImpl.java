/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Tuple;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilder;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilderExtensions;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilderImpl;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.AbstractPipeline;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.destination.DestinationConnector;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.SourceConnector;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.ChannelMiddleware;
import com.syncprem.uprising.pipeline.core.processors.NullChannelMiddleware;

import java.util.HashMap;
import java.util.Map;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class PipelineImpl extends AbstractPipeline
{
	//@DependencyInjection
	public PipelineImpl()
	{
	}

	@Override
	protected Context createContextInternal() throws Exception
	{
		Context context;

		final PipelineConfiguration pipelineConfiguration = this.getConfiguration();

		if (pipelineConfiguration == null ||
				pipelineConfiguration.getContextClass() == null)
			context = new ContextImpl(); // fallback
		else
			context = Utils.newObjectFromClass(pipelineConfiguration.getContextClass());

		context.setConfiguration(pipelineConfiguration);
		context.create();

		return context;
	}

	@Override
	protected long executeInternal(Context context) throws Exception
	{
		Channel channel;

		SourceConnector<? extends ComponentSpecificConfiguration> sourceConnector;
		DestinationConnector<? extends ComponentSpecificConfiguration> destinationConnector;

		Class<? extends SourceConnector<? extends ComponentSpecificConfiguration>> sourceConnectorClass;
		Class<? extends DestinationConnector<? extends ComponentSpecificConfiguration>> destinationConnectorClass;
		Map<UntypedComponentConfiguration, Class<? extends ChannelMiddleware<? extends ComponentSpecificConfiguration>>> channelMiddlewareTypeConfigMappings;

		if (context == null)
			throw new ArgumentNullException("context");

		sourceConnectorClass = this.getConfiguration().getSourceConfiguration().getComponentClass();
		destinationConnectorClass = this.getConfiguration().getDestinationConfiguration().getComponentClass();
		channelMiddlewareTypeConfigMappings = new HashMap<>();

		for (UntypedComponentConfiguration middlewareConfiguration : this.getConfiguration().getMiddlewareConfigurations())
		{
			Class<? extends ChannelMiddleware<? extends ComponentSpecificConfiguration>> middlewareClass;

			if (middlewareConfiguration == null)
				continue;

			middlewareClass = middlewareConfiguration.getComponentClass();

			if (middlewareClass == null)
				continue;

			if (!ChannelMiddleware.class.isAssignableFrom(middlewareClass))
				continue;

			channelMiddlewareTypeConfigMappings.put(middlewareConfiguration, middlewareClass);
		}

		failFastOnlyWhen(sourceConnectorClass == null, "sourceConnectorClass == null");

		sourceConnector = Utils.newObjectFromClass(sourceConnectorClass);

		failFastOnlyWhen(sourceConnector == null, "sourceConnector == null");

		failFastOnlyWhen(destinationConnectorClass == null, "destinationConnectorClass == null");

		destinationConnector = Utils.newObjectFromClass(destinationConnectorClass);

		failFastOnlyWhen(destinationConnector == null, "destinationConnector == null");

		try (sourceConnector)
		{
			sourceConnector.setConfiguration(this.getConfiguration().getSourceConfiguration());
			sourceConnector.create();

			try (destinationConnector)
			{
				destinationConnector.setConfiguration(this.getConfiguration().getDestinationConfiguration());
				destinationConnector.create();

				{
					RecordConfiguration configuration;

					// ----- START REFACTOR -----

					MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Channel> channelMiddlewareDelegate;
					MiddlewareBuilderImpl<Tuple.Tuple2<Context, RecordConfiguration>, Channel, UntypedComponentConfiguration> channelMiddlewareBuilderImpl;
					MiddlewareBuilder<Tuple.Tuple2<Context, RecordConfiguration>, Channel> channelMiddlewareBuilder;
					MiddlewareBuilderExtensions<Tuple.Tuple2<Context, RecordConfiguration>, Channel, UntypedComponentConfiguration> channelMiddlewareBuilderExtensions;

					configuration = this.getConfiguration().getRecordConfiguration();

					if (configuration == null)
						configuration = new RecordConfiguration();

					sourceConnector.preExecute(context, configuration);
					destinationConnector.preExecute(context, configuration);

					channelMiddlewareBuilderImpl = new MiddlewareBuilderImpl<>();
					channelMiddlewareBuilder = channelMiddlewareBuilderImpl;
					channelMiddlewareBuilderExtensions = channelMiddlewareBuilderImpl;

					if (false)
					{
						// object instance
						final NullChannelMiddleware nullChannelMiddleware = new NullChannelMiddleware();
						nullChannelMiddleware.setConfiguration(new UntypedComponentConfiguration());
						nullChannelMiddleware.create();
						channelMiddlewareBuilderExtensions.with(nullChannelMiddleware);

						// regular methods
						channelMiddlewareBuilder.use(NullChannelMiddleware::nullChannelMiddlewareMethod);

						// lambda expressions
						channelMiddlewareBuilder.use(next ->
						{
							return (_data, _channel) ->
							{
								if (next != null)
									return next.invoke(_data, _channel);
								else
									return _channel;
							};
						});
					}

					// by processor class (reflection)
					for (Map.Entry<UntypedComponentConfiguration, Class<? extends ChannelMiddleware<? extends ComponentSpecificConfiguration>>> channelMiddlewareTypeConfigMapping : channelMiddlewareTypeConfigMappings.entrySet())
					{
						if (channelMiddlewareTypeConfigMapping == null)
							continue;

						if (channelMiddlewareTypeConfigMapping.getKey() == null)
							throw new InvalidOperationException("channelMiddlewareTypeConfigMapping.isKey()");

						if (channelMiddlewareTypeConfigMapping.getValue() == null)
							throw new InvalidOperationException("channelMiddlewareTypeConfigMapping.getValue()");

						channelMiddlewareBuilderExtensions.from(channelMiddlewareTypeConfigMapping.getValue(), channelMiddlewareTypeConfigMapping.getKey());
					}

					channelMiddlewareDelegate = channelMiddlewareBuilder.build();

					// ----- END REFACTOR -----

					failFastOnlyWhen(channelMiddlewareDelegate == null, "processor == null");

					channel = sourceConnector.produce(context, configuration);

					try (Channel _channel = channel) // disposal outer-most channel
					{
						channel = channelMiddlewareDelegate.invoke(new Tuple.Tuple2<>(context, configuration), channel);

						destinationConnector.consume(context, configuration, channel);
					}

					destinationConnector.postExecute(context, configuration);
					sourceConnector.postExecute(context, configuration);
				}
			}
		}

		return 0L;
	}
}

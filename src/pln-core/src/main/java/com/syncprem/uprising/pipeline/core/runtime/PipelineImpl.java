/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
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
import com.syncprem.uprising.pipeline.abstractions.stage.interceptor.Interceptor;
import com.syncprem.uprising.pipeline.core.interceptors.NullInterceptor;

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
		Map<UntypedComponentConfiguration, Class<? extends Interceptor<? extends ComponentSpecificConfiguration>>> interceptorTypeConfigMappings;

		if (context == null)
			throw new ArgumentNullException("context");

		sourceConnectorClass = this.getConfiguration().getSourceConfiguration().getComponentClass();
		destinationConnectorClass = this.getConfiguration().getDestinationConfiguration().getComponentClass();
		interceptorTypeConfigMappings = new HashMap<>();

		for (UntypedComponentConfiguration interceptorConfiguration : this.getConfiguration().getInterceptorConfigurations())
		{
			Class<? extends Interceptor<? extends ComponentSpecificConfiguration>> interceptorClass;

			if (interceptorConfiguration == null)
				continue;

			interceptorClass = interceptorConfiguration.getComponentClass();

			if (interceptorClass == null)
				continue;

			/*if (!interceptorClass.isAssignableFrom(Interceptor.class))
				continue;*/

			if (!Interceptor.class.isAssignableFrom(interceptorClass))
				continue;

			interceptorTypeConfigMappings.put(interceptorConfiguration, interceptorClass);
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

					MiddlewareDelegate<Channel> interceptor;
					MiddlewareBuilderImpl<Channel, UntypedComponentConfiguration> interceptorBuilderImpl;
					MiddlewareBuilder<Channel> interceptorBuilder;
					MiddlewareBuilderExtensions<Channel, UntypedComponentConfiguration> interceptorBuilderExtensions;

					configuration = this.getConfiguration().getRecordConfiguration();

					if (configuration == null)
						configuration = new RecordConfiguration();

					sourceConnector.preExecute(context, configuration);
					destinationConnector.preExecute(context, configuration);

					interceptorBuilderImpl = new MiddlewareBuilderImpl<>();
					interceptorBuilder = interceptorBuilderImpl;
					interceptorBuilderExtensions = interceptorBuilderImpl;

					if (false)
					{
						// object instance
						final NullInterceptor nullInterceptor = new NullInterceptor();
						nullInterceptor.setConfiguration(new UntypedComponentConfiguration());
						nullInterceptor.create();
						interceptorBuilderExtensions.with(nullInterceptor);

						// regular methods
						interceptorBuilder.use(NullInterceptor::nullInterceptorMethod);

						// lambda expressions
						interceptorBuilder.use(next ->
						{
							return (_context, _configuration, _channel) ->
							{
								if (next != null)
									return next.invoke(_context, _configuration, _channel);
								else
									return _channel;
							};
						});
					}

					// by interceptor class (reflection)
					for (Map.Entry<UntypedComponentConfiguration, Class<? extends Interceptor<? extends ComponentSpecificConfiguration>>> interceptorTypeConfigMapping : interceptorTypeConfigMappings.entrySet())
					{
						if (interceptorTypeConfigMapping == null)
							continue;

						if (interceptorTypeConfigMapping.getKey() == null)
							throw new InvalidOperationException("interceptorTypeConfigMapping.isKey()");

						if (interceptorTypeConfigMapping.getValue() == null)
							throw new InvalidOperationException("interceptorTypeConfigMapping.getValue()");

						interceptorBuilderExtensions.from(interceptorTypeConfigMapping.getValue(), interceptorTypeConfigMapping.getKey());
					}

					interceptor = interceptorBuilder.build();

					// ----- END REFACTOR -----

					failFastOnlyWhen(interceptor == null, "interceptor == null");

					channel = sourceConnector.produce(context, configuration);

					try (Channel _channel = channel) // disposal outer-most channel
					{
						channel = interceptor.invoke(context, configuration, channel);

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

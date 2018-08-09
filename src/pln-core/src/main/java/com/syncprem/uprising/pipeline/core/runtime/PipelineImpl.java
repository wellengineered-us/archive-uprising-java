/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedStageConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.AbstractPipeline;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.destination.DestinationConnector;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.source.SourceConnector;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.*;
import com.syncprem.uprising.pipeline.core.processors.NullProcessor;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import java.util.HashMap;
import java.util.Map;

public final class PipelineImpl extends AbstractPipeline
{
	//@DependencyInjection
	public PipelineImpl()
	{
	}

	@Override
	protected Context createContextInternal()
	{
		return new ContextImpl();
	}

	@Override
	protected long executeInternal(Context context) throws Exception
	{
		Channel channel;

		SourceConnector<? extends StageSpecificConfiguration> sourceConnector;
		DestinationConnector<? extends StageSpecificConfiguration> destinationConnector;

		Class<? extends SourceConnector<? extends StageSpecificConfiguration>> sourceConnectorClass;
		Class<? extends DestinationConnector<? extends StageSpecificConfiguration>> destinationConnectorType;
		Map<UntypedStageConfiguration, Class<? extends Processor<? extends StageSpecificConfiguration>>> processorTypeConfigMappings;

		if (context == null)
			throw new ArgumentNullException("context");

		sourceConnectorClass = this.getConfiguration().getSourceConfiguration().getStageClass();
		destinationConnectorType = this.getConfiguration().getDestinationConfiguration().getStageClass();
		processorTypeConfigMappings = new HashMap<>();

		for (UntypedStageConfiguration processorConfiguration : this.getConfiguration().getProcessorConfigurations())
		{
			Class<? extends Processor<? extends StageSpecificConfiguration>> processorClass;

			if (processorConfiguration == null)
				continue;

			processorClass = processorConfiguration.getStageClass();

			if (processorClass == null)
				continue;

			processorTypeConfigMappings.put(processorConfiguration, processorClass);
		}

		if (sourceConnectorClass == null)
			throw new SyncPremException("sourceConnectorClass");

		sourceConnector = Utils.newObjectFromClass(sourceConnectorClass);

		if (sourceConnector == null)
			throw new InvalidOperationException("sourceConnector");

		if (destinationConnectorType == null)
			throw new InvalidOperationException("destinationConnectorType");

		destinationConnector = Utils.newObjectFromClass(destinationConnectorType);

		if (destinationConnector == null)
			throw new InvalidOperationException("destinationConnector");

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

					ProcessDelegate process;
					ProcessorBuilderImpl processorBuilderImpl;
					ProcessorBuilder processorBuilder;
					ProcessorBuilderExtensions processorBuilderExtensions;

					configuration = this.getConfiguration().getRecordConfiguration();

					if (configuration == null)
						configuration = new RecordConfiguration();

					sourceConnector.preExecute(context, configuration);
					destinationConnector.preExecute(context, configuration);

					processorBuilderImpl = new ProcessorBuilderImpl();
					processorBuilder = processorBuilderImpl;
					processorBuilderExtensions = processorBuilderImpl;

					if (false)
					{
						// object instance
						processorBuilderExtensions.from(new NullProcessor(), new UntypedStageConfiguration());

						// regular methods
						processorBuilder.use(NullProcessor::nullMiddlewareMethod);

						// lambda expressions
						processorBuilder.use(next ->
						{
							return (_context, _configuration, _channel) ->
							{
								System.out.println("processor_first");
								return next.invoke(_context, _configuration, _channel);
							};
						});
					}

					// by processor class (reflection)
					for (Map.Entry<UntypedStageConfiguration, Class<? extends Processor<? extends StageSpecificConfiguration>>> processorTypeConfigMapping : processorTypeConfigMappings.entrySet())
					{
						if (processorTypeConfigMapping == null)
							continue;

						if (processorTypeConfigMapping.getKey() == null)
							throw new InvalidOperationException("processorTypeConfigMapping.isKey()");

						if (processorTypeConfigMapping.getValue() == null)
							throw new InvalidOperationException("processorTypeConfigMapping.getValue()");

						processorBuilderExtensions.from(processorTypeConfigMapping.getValue(), processorTypeConfigMapping.getKey());
					}

					process = processorBuilder.build();

					if (process == null)
						throw new InvalidOperationException("process");

					channel = sourceConnector.produce(context, configuration);

					channel = process.invoke(context, configuration, channel);

					destinationConnector.consume(context, configuration, channel);

					destinationConnector.postExecute(context, configuration);
					sourceConnector.postExecute(context, configuration);
				}
			}
		}

		return 0L;
	}
}

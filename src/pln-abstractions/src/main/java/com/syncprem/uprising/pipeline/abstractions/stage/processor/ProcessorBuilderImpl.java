/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedStageConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import java.util.ArrayList;
import java.util.List;

public final class ProcessorBuilderImpl implements ProcessorBuilder, ProcessorBuilderExtensions
{
	public ProcessorBuilderImpl()
	{
		this(new ArrayList<>());
	}

	public ProcessorBuilderImpl(List<ChainDelegate<ProcessDelegate, ProcessDelegate>> components)
	{
		if (components == null)
			throw new ArgumentNullException("components");

		this.components = components;
	}

	private ProcessorBuilderImpl(ProcessorBuilder processorBuilder)
	{
		this();

		if (processorBuilder == null)
			throw new ArgumentNullException("processorBuilder");
	}

	private final List<ChainDelegate<ProcessDelegate, ProcessDelegate>> components;

	private List<ChainDelegate<ProcessDelegate, ProcessDelegate>> getComponents()
	{
		return this.components;
	}

	@Override
	public ProcessDelegate build()
	{
		ProcessDelegate transform = (context, configuration, channel) -> channel; // simply return original runtime unmodified

		// REVERSE LIST - LIFO order
		for (int i = this.getComponents().size() - 1; i >= 0; i--)
		{
			final ChainDelegate<ProcessDelegate, ProcessDelegate> component = this.getComponents().get(i);
			final ProcessDelegate _transform = transform;

			if (component == null)
				continue;

			transform = component.invoke(_transform);
		}

		return transform;
	}

	@Override
	public ProcessorBuilderImpl from(Processor<? extends StageSpecificConfiguration> processor, UntypedStageConfiguration stageConfiguration) throws Exception
	{
		if (processor == null)
			throw new ArgumentNullException("processor");

		if (stageConfiguration == null)
			throw new ArgumentNullException("stageConfiguration");

		return this.use(next ->
		{
			return (context, configuration, channel) ->
			{
				final Processor<? extends StageSpecificConfiguration> _processor = processor; // prevent closure bug
				final UntypedStageConfiguration _stageConfiguration = stageConfiguration; // prevent closure bug
				Channel newChannel;

				if (_processor == null)
					throw new InvalidOperationException("_processor");

				if (_stageConfiguration == null)
					throw new InvalidOperationException("_stageConfiguration");

				try (_processor)
				{
					// should we assume the instance is already setup?
					if (!_processor.isCreated())
					{
						_processor.setConfiguration(_stageConfiguration);
						_processor.create();
					}

					_processor.preExecute(context, configuration);
					newChannel = _processor.process(context, configuration, channel, next);
					_processor.postExecute(context, configuration);

					return newChannel;
				}
				catch (Exception ex)
				{
					throw new SyncPremException(ex);
				}
			};
		});
	}

	@Override
	public ProcessorBuilder from(Class<? extends Processor<? extends StageSpecificConfiguration>> processorClass, UntypedStageConfiguration stageConfiguration) throws Exception
	{
		if (processorClass == null)
			throw new ArgumentNullException("processorClass");

		if (stageConfiguration == null)
			throw new ArgumentNullException("stageConfiguration");

		return this.use(next ->
		{
			return (context, configuration, channel) ->
			{
				final Class<? extends Processor<? extends StageSpecificConfiguration>> _processorClass = processorClass; // prevent closure bug
				final UntypedStageConfiguration _stageConfiguration = stageConfiguration; // prevent closure bug

				Channel newChannel;

				if (_processorClass == null)
					throw new InvalidOperationException("_processorClass");

				if (_stageConfiguration == null)
					throw new InvalidOperationException("_stageConfiguration");

				try (Processor<? extends StageSpecificConfiguration> processor = Utils.newObjectFromClass(_processorClass);)
				{
					if (processor == null)
						throw new InvalidOperationException("processor");

					processor.setConfiguration(_stageConfiguration);
					processor.create();

					processor.preExecute(context, configuration);
					newChannel = processor.process(context, configuration, channel, next);
					processor.postExecute(context, configuration);

					return newChannel;
				}
				catch (Exception ex)
				{
					throw new SyncPremException(ex);
				}
			};
		});
	}

	@Override
	public ProcessorBuilderImpl new_()
	{
		return new ProcessorBuilderImpl(this);
	}

	@Override
	public ProcessorBuilderImpl use(ChainDelegate<ProcessDelegate, ProcessDelegate> middleware)
	{
		if (middleware == null)
			throw new ArgumentNullException("middleware");

		this.getComponents().add(middleware);
		return this;
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilder;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilderExtensions;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilderImpl;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.processor.StreamProcessor;
import com.syncprem.uprising.pipeline.abstractions.runtime.*;
import com.syncprem.uprising.pipeline.core.processors.NullStreamProcessor;
import com.syncprem.uprising.streamingio.primitives.*;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.*;

public final class ContextImpl extends AbstractContext
{
	public ContextImpl()
	{
	}

	@Override
	protected Channel createChannelInternal(LifecycleIterator<Record> records) throws Exception
	{
		Channel channel;

		if (records == null)
			throw new ArgumentNullException("records");

		// ----- START REFACTOR -----

		MiddlewareDelegate<Record> streamProcess;
		MiddlewareBuilderImpl<Record, UntypedComponentConfiguration> streamProcessorBuilderImpl;
		MiddlewareBuilder<Record> streamProcessorBuilder;
		MiddlewareBuilderExtensions<Record, UntypedComponentConfiguration> streamProcessorBuilderExtensions;

		Map<UntypedComponentConfiguration, Class<? extends StreamProcessor<? extends ComponentSpecificConfiguration>>> streamProcessorTypeConfigMappings;

		final RecordConfiguration configuration = new RecordConfiguration();
		streamProcessorBuilderImpl = new MiddlewareBuilderImpl<>();
		streamProcessorBuilder = streamProcessorBuilderImpl;
		streamProcessorBuilderExtensions = streamProcessorBuilderImpl;

		streamProcessorTypeConfigMappings = new HashMap<>();

		for (UntypedComponentConfiguration streamProcessorConfiguration : this.getConfiguration().getInterceptorConfigurations())
		{
			Class<? extends StreamProcessor<? extends ComponentSpecificConfiguration>> streamProcessorClass;

			if (streamProcessorConfiguration == null)
				continue;

			streamProcessorClass = streamProcessorConfiguration.getComponentClass();

			if (streamProcessorClass == null)
				continue;

			/*if (!streamProcessorClass.isAssignableFrom(StreamProcessor.class))
				continue;*/

			if (!StreamProcessor.class.isAssignableFrom(streamProcessorClass))
				continue;

			streamProcessorTypeConfigMappings.put(streamProcessorConfiguration, streamProcessorClass);
		}

		final NullStreamProcessor nullStreamProcessor = new NullStreamProcessor();

		if (false)
		{
			// object instance
			nullStreamProcessor.setConfiguration(new UntypedComponentConfiguration());
			nullStreamProcessor.create();
			streamProcessorBuilderExtensions.with(nullStreamProcessor);

			// regular methods
			streamProcessorBuilder.use(NullStreamProcessor::nullStreamProcessorMethod);

			// lambda expressions
			streamProcessorBuilder.use(next ->
			{
				return (_context, _configuration, _record) ->
				{
					if (next != null)
						return next.invoke(_context, _configuration, _record);
					else
						return _record;
				};
			});
		}

		// by interceptor class (reflection)
		for (Map.Entry<UntypedComponentConfiguration, Class<? extends StreamProcessor<? extends ComponentSpecificConfiguration>>> streamProcessorTypeConfigMapping : streamProcessorTypeConfigMappings.entrySet())
		{
			if (streamProcessorTypeConfigMapping == null)
				continue;

			if (streamProcessorTypeConfigMapping.getKey() == null)
				throw new InvalidOperationException("streamProcessorTypeConfigMapping.isKey()");

			if (streamProcessorTypeConfigMapping.getValue() == null)
				throw new InvalidOperationException("streamProcessorTypeConfigMapping.getValue()");

			streamProcessorBuilderExtensions.from(streamProcessorTypeConfigMapping.getValue(), streamProcessorTypeConfigMapping.getKey());
		}

		streamProcess = streamProcessorBuilder.build();

		// ----- END REFACTOR -----

		failFastOnlyWhen(streamProcess == null, "streamProcess == null");

		records = WrappedIteratorExtensions.getWrappedIterator(records, "channel.records", (index, item) ->
		{
			if (item == null)
				throw new ArgumentNullException("item");

			item.setIndex(index);
			item = streamProcess.invoke(this, configuration, item);

			return item;
		}, (punctuateModulo, sourceLabel, itemIndex, isCompleted, rollingTiming) ->
		{
			if (isCompleted)
				Utils.safeDispose(nullStreamProcessor);

			//if (itemIndex == WrappedIteratorImpl.DEFAULT_INDEX || isCompleted)
			System.out.println(String.format("[(progress) %s@%s-%s: itemIndex = %s (%s#), isCompleted = %s, rollingTiming = %sms]", sourceLabel, formatCurrentThreadId(), formatUUID(this.getComponentId()), itemIndex, itemIndex + 1, isCompleted, rollingTiming));
		});

		final StreamFactory streamFactory = this;
		final Stream stream = streamFactory.createStream(records);
		channel = new ChannelImpl(stream);
		channel.create();

		return channel;
	}

	@Override
	protected Channel createEmptyChannelInternal() throws Exception
	{
		Channel channel;

		final Stream stream = this.createEmptyStream();
		channel = this.createChannel(stream);

		return channel;
	}

	@Override
	protected Record createEmptyRecordInternal() throws Exception
	{
		return RecordImpl.EMPTY;
	}

	@Override
	protected Stream createEmptyStreamInternal() throws Exception
	{
		Stream stream;
		LifecycleIterator<Record> records;
		final List<Record> temp = Collections.emptyList();

		records = WrappedIteratorExtensions.toLifecycleIterator(temp.iterator());
		stream = new StreamImpl(records);

		return stream;
	}

	@Override
	protected Record createRecordInternal(Schema schema, Payload payload, String topic, Partition partition, Offset offset) throws Exception
	{
		return new RecordImpl(schema, payload, Utils.EMPTY_STRING, PartitionImpl.NONE, OffsetImpl.NONE);
	}

	@Override
	protected Stream createStreamInternal(LifecycleIterator<Record> records) throws Exception
	{
		return new StreamImpl(records);
	}
}

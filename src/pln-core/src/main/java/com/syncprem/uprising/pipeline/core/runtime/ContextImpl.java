/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilder;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilderExtensions;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareBuilderImpl;
import com.syncprem.uprising.pipeline.abstractions.middleware.MiddlewareDelegate;
import com.syncprem.uprising.pipeline.abstractions.runtime.*;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.RecordMiddleware;
import com.syncprem.uprising.pipeline.abstractions.stage.processor.StreamMiddleware;
import com.syncprem.uprising.pipeline.core.processors.NullRecordMiddleware;
import com.syncprem.uprising.pipeline.core.processors.NullStreamMiddleware;
import com.syncprem.uprising.streamingio.primitives.*;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;

import java.util.*;

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

		MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Record> recordMiddlewareDelegate;
		MiddlewareBuilderImpl<Tuple.Tuple2<Context, RecordConfiguration>, Record, UntypedComponentConfiguration> recordMiddlewareBuilderImpl;
		MiddlewareBuilder<Tuple.Tuple2<Context, RecordConfiguration>, Record> recordMiddlewareBuilder;
		MiddlewareBuilderExtensions<Tuple.Tuple2<Context, RecordConfiguration>, Record, UntypedComponentConfiguration> recordMiddlewareBuilderExtensions;

		Map<UntypedComponentConfiguration, Class<? extends RecordMiddleware<? extends ComponentSpecificConfiguration>>> recordMiddlewareTypeConfigMappings;

		final RecordConfiguration configuration = new RecordConfiguration();
		recordMiddlewareBuilderImpl = new MiddlewareBuilderImpl<>();
		recordMiddlewareBuilder = recordMiddlewareBuilderImpl;
		recordMiddlewareBuilderExtensions = recordMiddlewareBuilderImpl;

		recordMiddlewareTypeConfigMappings = new LinkedHashMap<>();

		for (UntypedComponentConfiguration recordMiddlewareConfiguration : this.getConfiguration().getMiddlewareConfigurations())
		{
			Class<? extends RecordMiddleware<? extends ComponentSpecificConfiguration>> recordMiddlewareClass;

			if (recordMiddlewareConfiguration == null)
				continue;

			recordMiddlewareClass = recordMiddlewareConfiguration.getComponentClass();

			if (recordMiddlewareClass == null)
				continue;

			if (!RecordMiddleware.class.isAssignableFrom(recordMiddlewareClass))
				continue;

			recordMiddlewareTypeConfigMappings.put(recordMiddlewareConfiguration, recordMiddlewareClass);
		}

		final NullRecordMiddleware nullRecordMiddleware = new NullRecordMiddleware();

		if (false)
		{
			// object instance
			nullRecordMiddleware.setConfiguration(new UntypedComponentConfiguration());
			nullRecordMiddleware.create();
			recordMiddlewareBuilderExtensions.with(nullRecordMiddleware);

			// regular methods
			recordMiddlewareBuilder.use(NullRecordMiddleware::nullRecordMiddlewareMethod);

			// lambda expressions
			recordMiddlewareBuilder.use(next ->
			{
				return (_data, _record) ->
				{
					if (next != null)
						return next.invoke(_data, _record);
					else
						return _record;
				};
			});
		}

		// by processor class (reflection)
		for (Map.Entry<UntypedComponentConfiguration, Class<? extends RecordMiddleware<? extends ComponentSpecificConfiguration>>> recordMiddlewareTypeConfigMapping : recordMiddlewareTypeConfigMappings.entrySet())
		{
			if (recordMiddlewareTypeConfigMapping == null)
				continue;

			if (recordMiddlewareTypeConfigMapping.getKey() == null)
				throw new InvalidOperationException("recordMiddlewareTypeConfigMapping.isKey()");

			if (recordMiddlewareTypeConfigMapping.getValue() == null)
				throw new InvalidOperationException("recordMiddlewareTypeConfigMapping.getValue()");

			recordMiddlewareBuilderExtensions.from(recordMiddlewareTypeConfigMapping.getValue(), recordMiddlewareTypeConfigMapping.getKey());
		}

		recordMiddlewareDelegate = recordMiddlewareBuilder.build();

		// ----- END REFACTOR -----

		failFastOnlyWhen(recordMiddlewareDelegate == null, "streamProcess == null");

		records = WrappedIteratorExtensions.getWrappedIterator(records, "channel.records", (index, item) ->
		{
			if (item == null)
				throw new ArgumentNullException("item");

			item.setIndex(index);
			item = recordMiddlewareDelegate.invoke(new Tuple.Tuple2<>(this, configuration), item);

			return item;
		}, (punctuateModulo, sourceLabel, itemIndex, isCompleted, rollingTiming) ->
		{
			if (isCompleted)
				Utils.safeDispose(nullRecordMiddleware);

			//if (itemIndex == WrappedIteratorImpl.DEFAULT_INDEX || isCompleted)
			System.out.println(String.format("[(progress) %s@%s-%s: itemIndex = %s (%s#), isCompleted = %s, rollingTiming = %s sec(s)]", sourceLabel, formatCurrentThreadId(), formatUUID(this.getComponentId()), itemIndex, itemIndex + 1, isCompleted, rollingTiming));
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
		Stream stream;

		// ----- START REFACTOR -----

		MiddlewareDelegate<Tuple.Tuple2<Context, RecordConfiguration>, Stream> streamMiddlewareDelegate;
		MiddlewareBuilderImpl<Tuple.Tuple2<Context, RecordConfiguration>, Stream, UntypedComponentConfiguration> streamMiddlewareBuilderImpl;
		MiddlewareBuilder<Tuple.Tuple2<Context, RecordConfiguration>, Stream> streamMiddlewareBuilder;
		MiddlewareBuilderExtensions<Tuple.Tuple2<Context, RecordConfiguration>, Stream, UntypedComponentConfiguration> streamMiddlewareBuilderExtensions;

		Map<UntypedComponentConfiguration, Class<? extends StreamMiddleware<? extends ComponentSpecificConfiguration>>> streamMiddlewareTypeConfigMappings;

		final RecordConfiguration configuration = new RecordConfiguration();
		streamMiddlewareBuilderImpl = new MiddlewareBuilderImpl<>();
		streamMiddlewareBuilder = streamMiddlewareBuilderImpl;
		streamMiddlewareBuilderExtensions = streamMiddlewareBuilderImpl;

		streamMiddlewareTypeConfigMappings = new LinkedHashMap<>();

		for (UntypedComponentConfiguration streamMiddlewareConfiguration : this.getConfiguration().getMiddlewareConfigurations())
		{
			Class<? extends StreamMiddleware<? extends ComponentSpecificConfiguration>> streamMiddlewareClass;

			if (streamMiddlewareConfiguration == null)
				continue;

			streamMiddlewareClass = streamMiddlewareConfiguration.getComponentClass();

			if (streamMiddlewareClass == null)
				continue;

			if (!StreamMiddleware.class.isAssignableFrom(streamMiddlewareClass))
				continue;

			streamMiddlewareTypeConfigMappings.put(streamMiddlewareConfiguration, streamMiddlewareClass);
		}

		final NullStreamMiddleware nullStreamMiddleware = new NullStreamMiddleware();

		if (false)
		{
			// object instance
			nullStreamMiddleware.setConfiguration(new UntypedComponentConfiguration());
			nullStreamMiddleware.create();
			streamMiddlewareBuilderExtensions.with(nullStreamMiddleware);

			// regular methods
			streamMiddlewareBuilder.use(NullStreamMiddleware::nullStreamMiddlewareMethod);

			// lambda expressions
			streamMiddlewareBuilder.use(next ->
			{
				return (_data, _stream) ->
				{
					if (next != null)
						return next.invoke(_data, _stream);
					else
						return _stream;
				};
			});
		}

		// by processor class (reflection)
		for (Map.Entry<UntypedComponentConfiguration, Class<? extends StreamMiddleware<? extends ComponentSpecificConfiguration>>> streamMiddlewareTypeConfigMapping : streamMiddlewareTypeConfigMappings.entrySet())
		{
			if (streamMiddlewareTypeConfigMapping == null)
				continue;

			if (streamMiddlewareTypeConfigMapping.getKey() == null)
				throw new InvalidOperationException("streamMiddlewareTypeConfigMapping.isKey()");

			if (streamMiddlewareTypeConfigMapping.getValue() == null)
				throw new InvalidOperationException("streamMiddlewareTypeConfigMapping.getValue()");

			streamMiddlewareBuilderExtensions.from(streamMiddlewareTypeConfigMapping.getValue(), streamMiddlewareTypeConfigMapping.getKey());
		}

		streamMiddlewareDelegate = streamMiddlewareBuilder.build();

		// ----- END REFACTOR -----

		failFastOnlyWhen(streamMiddlewareDelegate == null, "streamProcess == null");

		stream = new StreamImpl(records);
		stream = streamMiddlewareDelegate.invoke(new Tuple.Tuple2<>(this, configuration), stream);

		return stream;
	}
}

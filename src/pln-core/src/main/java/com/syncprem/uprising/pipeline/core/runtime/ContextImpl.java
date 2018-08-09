/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.formatCurrentThreadId;
import static com.syncprem.uprising.infrastructure.polyfills.Utils.formatUUID;

public final class ContextImpl extends AbstractComponent implements Context
{
	public ContextImpl()
	{
		this(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
	}

	public ContextImpl(Map<String, Object> globalState, Map<Component, Map<String, Object>> localState)
	{
		if (globalState == null)
			throw new ArgumentNullException("globalState");

		if (localState == null)
			throw new ArgumentNullException("localState");

		this.globalState = globalState;
		this.localState = localState;
	}

	private final Map<String, Object> globalState;
	private final Map<Component, Map<String, Object>> localState;

	@Override
	public Map<String, Object> getGlobalState()
	{
		return this.globalState;
	}

	@Override
	public Map<Component, Map<String, Object>> getLocalState()
	{
		return this.localState;
	}

	@Override
	public Channel createChannel(LifecycleIterator<Record> records)
	{
		if (records == null)
			throw new ArgumentNullException("records");

		records = WrappedIteratorExtensions.getWrappedIterator(records, "channel.records", (index, item) ->
		{
			if (item == null)
				throw new ArgumentNullException("item");

			item.setIndex(index);
			return item;
		}, (punctuateModulo, sourceLabel, itemIndex, isCompleted, rollingTiming) ->
		{
			if (itemIndex == -1L || isCompleted)
				System.out.println(String.format("[(progress) %s@%s-%s: itemIndex = %s (%s#), isCompleted = %s, rollingTiming = %sms]", sourceLabel, formatCurrentThreadId(), formatUUID(this.getComponentId()), itemIndex, itemIndex + 1, isCompleted, rollingTiming));
		});

		return new ChannelImpl(records);
	}

	@Override
	public Channel createEmptyChannel()
	{
		LifecycleIterator<Record> records;
		final List<Record> temp = Collections.emptyList();

		records = WrappedIteratorExtensions.toLifecycleIterator(temp.iterator());

		records = WrappedIteratorExtensions.getWrappedIterator(records, "channel.records-0", (index, item) -> item, (punctuateModulo, sourceLabel, itemIndex, isCompleted, rollingTiming) ->
		{
			if (itemIndex == -1L || isCompleted)
				System.out.println(String.format("[(progress-0) %s@%s-%s: itemIndex = %s (%s#), isCompleted = %s, rollingTiming = %sms]", sourceLabel, formatCurrentThreadId(), formatUUID(this.getComponentId()), itemIndex, itemIndex + 1, isCompleted, rollingTiming));
		});

		return new ChannelImpl(records);
	}
}

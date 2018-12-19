/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.PipelineConfiguration;
import com.syncprem.uprising.streamingio.primitives.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractContext extends AbstractComponent implements Context
{
	protected AbstractContext()
	{
		this(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
	}

	protected AbstractContext(Map<String, Object> globalState, Map<Component, Map<String, Object>> localState)
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
	private PipelineConfiguration configuration;

	@Override
	public final PipelineConfiguration getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public final void setConfiguration(PipelineConfiguration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public final Map<String, Object> getGlobalState()
	{
		return this.globalState;
	}

	@Override
	public final Map<Component, Map<String, Object>> getLocalState()
	{
		return this.localState;
	}

	@Override
	public final Channel createChannel(LifecycleIterator<Record> records) throws SyncPremException
	{
		try
		{
			return this.createChannelInternal(records);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Channel createChannelInternal(LifecycleIterator<Record> records) throws Exception;

	@Override
	public final Channel createEmptyChannel() throws SyncPremException
	{
		try
		{
			return this.createEmptyChannelInternal();
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Channel createEmptyChannelInternal() throws Exception;

	@Override
	public final Record createEmptyRecord() throws SyncPremException
	{
		try
		{
			return this.createEmptyRecordInternal();
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Record createEmptyRecordInternal() throws Exception;

	@Override
	public final Stream createEmptyStream() throws SyncPremException
	{
		try
		{
			return this.createEmptyStreamInternal();
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Stream createEmptyStreamInternal() throws Exception;

	@Override
	public final Record createRecord(Schema schema, Payload payload, String topic, Partition partition, Offset offset) throws SyncPremException
	{
		try
		{
			return this.createRecordInternal(schema, payload, topic, partition, offset);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Record createRecordInternal(Schema schema, Payload payload, String topic, Partition partition, Offset offset) throws Exception;

	@Override
	public final Stream createStream(LifecycleIterator<Record> records) throws SyncPremException
	{
		try
		{
			return this.createStreamInternal(records);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract Stream createStreamInternal(LifecycleIterator<Record> records) throws Exception;
}

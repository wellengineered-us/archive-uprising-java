/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.configuration.ConfigurationObjectCollectionImpl;
import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.runtime.Host;

import java.util.ArrayList;
import java.util.List;

public class HostConfiguration extends ComponentConfiguration
{
	public HostConfiguration()
	{
		this.pipelineConfigurations = new ConfigurationObjectCollectionImpl<PipelineConfiguration>(this);
	}

	public HostConfiguration(ConfigurationObjectCollectionImpl<PipelineConfiguration> pipelineConfigurations)
	{
		if (pipelineConfigurations == null)
			throw new ArgumentNullException("pipelineConfigurations");

		this.pipelineConfigurations = pipelineConfigurations;
	}

	private final ConfigurationObjectCollectionImpl<PipelineConfiguration> pipelineConfigurations;
	private Boolean enableDispatchLoop;
	private String hostClassName;
	private String version;

	public Class<? extends Host> getHostClass()
	{
		return Utils.loadClassByName(this.getHostClassName(), Host.class);
	}

	public String getHostClassName()
	{
		return this.hostClassName;
	}

	public void setHostClassName(String hostClassName)
	{
		this.hostClassName = hostClassName;
	}

	public ConfigurationObjectCollectionImpl<PipelineConfiguration> getPipelineConfigurations()
	{
		return this.pipelineConfigurations;
	}

	public String getVersion()
	{
		return this.version;
	}

	public void setEnableDispatchLoop(Boolean enableDispatchLoop)
	{
		this.enableDispatchLoop = enableDispatchLoop;
	}

	public void setFieldName(String version)
	{
		this.version = version;
	}

	public Boolean enableDispatchLoop()
	{
		return this.enableDispatchLoop;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		Class<? extends Host> hostClass;
		Host host_;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getVersion()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Not a valid host configuration (version missing)."), Severity.ERROR));

		if (Utils.isNullOrEmptyString(this.getHostClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s host class name is required.", context), Severity.ERROR));
		else
		{
			hostClass = this.getHostClass();

			if (hostClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to load host host class by name.", context), Severity.ERROR));
			else if (!Host.class.isAssignableFrom(hostClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s loaded an unrecognized host host class by name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				host_ = Utils.newObjectFromClass(hostClass);

				if (host_ == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to instantiate host host class by name using default, public constructor.", context), Severity.ERROR));
			}
		}

		if (this.getPipelineConfigurations() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Pipeline configurations are required."), Severity.ERROR));
		else
			MessageImpl.addRange(messages, this.getPipelineConfigurations().validateAll("Pipeline"));

		return messages;
	}
}

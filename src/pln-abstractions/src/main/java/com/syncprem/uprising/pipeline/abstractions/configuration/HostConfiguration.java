/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.configuration.ConfigurationObjectCollectionImpl;
import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.runtime.Host;

import java.lang.module.ModuleDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	private String configurationVersion;
	private TimeUnit dispatchIdleTimeUnit;
	private Long dispatchIdleTimeValue;
	private Boolean enableDispatchLoop;
	private TimeUnit gracefulShutdownTimeUnit;
	private Long gracefulShutdownTimeValue;
	private String hostClassName;

	public String getConfigurationVersion()
	{
		return this.configurationVersion;
	}

	public void setConfigurationVersion(String configurationVersion)
	{
		this.configurationVersion = configurationVersion;
	}

	public TimeUnit getDispatchIdleTimeUnit()
	{
		return this.dispatchIdleTimeUnit;
	}

	public void setDispatchIdleTimeUnit(TimeUnit dispatchIdleTimeUnit)
	{
		this.dispatchIdleTimeUnit = dispatchIdleTimeUnit;
	}

	public Long getDispatchIdleTimeValue()
	{
		return this.dispatchIdleTimeValue;
	}

	public void setDispatchIdleTimeValue(Long dispatchIdleTimeValue)
	{
		this.dispatchIdleTimeValue = dispatchIdleTimeValue;
	}

	public TimeUnit getGracefulShutdownTimeUnit()
	{
		return this.gracefulShutdownTimeUnit;
	}

	public void setGracefulShutdownTimeUnit(TimeUnit gracefulShutdownTimeUnit)
	{
		this.gracefulShutdownTimeUnit = gracefulShutdownTimeUnit;
	}

	public Long getGracefulShutdownTimeValue()
	{
		return this.gracefulShutdownTimeValue;
	}

	public void setGracefulShutdownTimeValue(Long gracefulShutdownTimeValue)
	{
		this.gracefulShutdownTimeValue = gracefulShutdownTimeValue;
	}

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

	public void setEnableDispatchLoop(Boolean enableDispatchLoop)
	{
		this.enableDispatchLoop = enableDispatchLoop;
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

		final TryOut<ModuleDescriptor.Version> outVersion = new TryOut<>();
		final ModuleDescriptor.Version currentEngineVersion = ModuleDescriptor.Version.parse("0.2.0");
		final ModuleDescriptor.Version currentConfigurationVersion = ModuleDescriptor.Version.parse("2.0.0");
		final ModuleDescriptor.Version minimumConfigurationVersion = currentConfigurationVersion;

		if (Utils.isNullOrEmptyString(this.getConfigurationVersion()) ||
				!Utils.tryParseValid(this.getConfigurationVersion(), outVersion))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Not a valid host configuration (configurationVersion missing)."), Severity.ERROR));
		else if (!outVersion.isSet() || minimumConfigurationVersion == null ||
				outVersion.getValue().compareTo(minimumConfigurationVersion) < 0)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Host configuration configurationVersion '%s' is less than the required minimum configuration configurationVersion '%s'.", outVersion.getValue(), minimumConfigurationVersion), Severity.ERROR));

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

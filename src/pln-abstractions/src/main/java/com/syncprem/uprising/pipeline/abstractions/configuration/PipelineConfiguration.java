/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.configuration.ConfigurationObjectCollectionImpl;
import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class PipelineConfiguration extends ComponentConfiguration
{
	public PipelineConfiguration()
	{
		this.middlewareConfigurations = new ConfigurationObjectCollectionImpl<UntypedComponentConfiguration>(this);
	}

	public PipelineConfiguration(ConfigurationObjectCollectionImpl<UntypedComponentConfiguration> middlewareConfigurations)
	{
		if (middlewareConfigurations == null)
			throw new ArgumentNullException("middlewareConfigurations");

		this.middlewareConfigurations = middlewareConfigurations;
	}

	private final ConfigurationObjectCollectionImpl<UntypedComponentConfiguration> middlewareConfigurations;
	private String contextClassName;
	private UntypedComponentConfiguration destinationConfiguration;
	private Boolean isEnabled;
	private String pipelineClassName;
	private RecordConfiguration recordConfiguration;
	private UntypedComponentConfiguration sourceConfiguration;

	public Class<? extends Context> getContextClass()
	{
		return Utils.loadClassByName(this.getContextClassName(), Context.class);
	}

	public String getContextClassName()
	{
		return this.contextClassName;
	}

	public void setContextClassName(String contextClassName)
	{
		this.contextClassName = contextClassName;
	}

	public UntypedComponentConfiguration getDestinationConfiguration()
	{
		return this.destinationConfiguration;
	}

	public void setDestinationConfiguration(UntypedComponentConfiguration destinationConfiguration)
	{
		this.destinationConfiguration = destinationConfiguration;
	}

	public ConfigurationObjectCollectionImpl<UntypedComponentConfiguration> getMiddlewareConfigurations()
	{
		return this.middlewareConfigurations;
	}

	public Class<? extends Pipeline> getPipelineClass()
	{
		return Utils.loadClassByName(this.getPipelineClassName(), Pipeline.class);
	}

	public String getPipelineClassName()
	{
		return this.pipelineClassName;
	}

	public void setPipelineClassName(String pipelineClassName)
	{
		this.pipelineClassName = pipelineClassName;
	}

	public RecordConfiguration getRecordConfiguration()
	{
		return this.recordConfiguration;
	}

	public void setRecordConfiguration(RecordConfiguration recordConfiguration)
	{
		this.recordConfiguration = recordConfiguration;
	}

	public UntypedComponentConfiguration getSourceConfiguration()
	{
		return this.sourceConfiguration;
	}

	public void setSourceConfiguration(UntypedComponentConfiguration sourceConfiguration)
	{
		this.sourceConfiguration = sourceConfiguration;
	}

	public void setEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public Boolean isEnabled()
	{
		return this.isEnabled;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		Class<? extends Context> contextClass;
		Context context_;
		Class<? extends Pipeline> pipelineClass;
		Pipeline pipeline_;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getPipelineClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s pipeline pipeline class name is required.", context), Severity.ERROR));
		else
		{
			pipelineClass = this.getPipelineClass();

			if (pipelineClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to load pipeline pipeline class by name.", context), Severity.ERROR));
			else if (!Pipeline.class.isAssignableFrom(pipelineClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s loaded an unrecognized pipeline pipeline class by name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				pipeline_ = Utils.newObjectFromClass(pipelineClass);

				if (pipeline_ == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to instantiate pipeline pipeline class by name using default, public constructor.", context), Severity.ERROR));
			}
		}

		if (Utils.isNullOrEmptyString(this.getContextClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s pipeline context class name is required.", context), Severity.ERROR));
		else
		{
			contextClass = this.getContextClass();

			if (contextClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to load pipeline context class by name.", context), Severity.ERROR));
			else if (!Context.class.isAssignableFrom(contextClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s loaded an unrecognized pipeline context class by name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				context_ = Utils.newObjectFromClass(contextClass);

				if (context_ == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to instantiate pipeline context class by name using default, public constructor.", context), Severity.ERROR));
			}
		}

		if (this.getSourceConfiguration() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Source configuration is required."), Severity.ERROR));
		else
			MessageImpl.addRange(messages, this.getSourceConfiguration().validate(String.format("%s/%s", context, "Source")));

		if (this.getMiddlewareConfigurations() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("ChannelMiddleware configuration is required."), Severity.ERROR));
		else
			MessageImpl.addRange(messages, this.getMiddlewareConfigurations().validateAll(String.format("%s/%s", context, "ChannelMiddleware")));

		if (this.getDestinationConfiguration() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Destination configuration is required."), Severity.ERROR));
		else
			MessageImpl.addRange(messages, this.getDestinationConfiguration().validate(String.format("%s/%s", context, "Destination")));

		return messages;
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.serialization.JsonSerializationStrategyImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypedComponentConfiguration<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends UntypedComponentConfiguration
{
	public TypedComponentConfiguration(UntypedComponentConfiguration componentConfiguration, Class<? extends TComponentSpecificConfiguration> typedComponentSpecificConfigurationClass)
	{
		this(new HashMap<>(), componentConfiguration, typedComponentSpecificConfigurationClass);
	}

	public TypedComponentConfiguration(Map<String, Object> componentSpecificConfiguration, UntypedComponentConfiguration componentConfiguration, Class<? extends TComponentSpecificConfiguration> typedComponentSpecificConfigurationClass)
	{
		super(componentSpecificConfiguration, typedComponentSpecificConfigurationClass);

		if (componentConfiguration == null)
			throw new ArgumentNullException("componentConfiguration");

		if (typedComponentSpecificConfigurationClass == null)
			throw new ArgumentNullException("typedComponentSpecificConfigurationClass");

		if (this.getComponentSpecificConfiguration() != null &&
				componentConfiguration.getComponentSpecificConfiguration() != null)
		{
			final Map<String, Object> otherComponentSpecificConfiguration = componentConfiguration.getComponentSpecificConfiguration();
			for (Map.Entry<String, Object> entry : otherComponentSpecificConfiguration.entrySet())
			{
				if (entry == null)
					continue;

				this.getComponentSpecificConfiguration().put(entry.getKey(), entry.getValue());
			}
		}

		this.setComponentClassName(componentConfiguration.getComponentClassName());
		this.setParent(componentConfiguration.getParent());
		this.setSurround(componentConfiguration.getSurround());

		this.typedComponentSpecificConfigurationClass = typedComponentSpecificConfigurationClass;
	}

	private final Class<? extends TComponentSpecificConfiguration> typedComponentSpecificConfigurationClass;
	private TComponentSpecificConfiguration typedComponentSpecificConfiguration;

	public TComponentSpecificConfiguration getTypedComponentSpecificConfiguration()
	{
		this.applyTypedComponentSpecificConfiguration(); // special case
		return this.typedComponentSpecificConfiguration;
	}

	public void setTypedComponentSpecificConfiguration(TComponentSpecificConfiguration typedComponentSpecificConfiguration)
	{
		this.ensureParentOnSetter(this.typedComponentSpecificConfiguration, typedComponentSpecificConfiguration);
		this.typedComponentSpecificConfiguration = typedComponentSpecificConfiguration;
	}

	public Class<? extends TComponentSpecificConfiguration> getTypedComponentSpecificConfigurationClass()
	{
		//typeof(TComponentSpecificConfiguration);
		return this.typedComponentSpecificConfigurationClass;
	}

	private void applyTypedComponentSpecificConfiguration(Class<? extends TComponentSpecificConfiguration> clazz)
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (this.getComponentSpecificConfiguration() != null)
		{
			TComponentSpecificConfiguration temp;

			temp = JsonSerializationStrategyImpl.getObjectFromJsonMap(this.getComponentSpecificConfiguration(), clazz);

			//if (temp == null)
			//temp = Utils.newObjectFromClass(clazz);

			this.setTypedComponentSpecificConfiguration(temp);
		}
	}

	public final void applyTypedComponentSpecificConfiguration()
	{
		this.applyTypedComponentSpecificConfiguration(this.getTypedComponentSpecificConfigurationClass());
	}

	public void resetTypedComponentSpecificConfiguration()
	{
		this.getComponentSpecificConfiguration().clear();
		this.setTypedComponentSpecificConfiguration(null);
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		MessageImpl.addRange(messages, super.validate(context));

		if (this.getComponentSpecificConfiguration() != null)
			MessageImpl.addRange(messages, this.validateTypedComponentSpecificConfiguration(context));

		return messages;
	}

	public final Iterable<Message> validateTypedComponentSpecificConfiguration(Object context)
	{
		if (this.getComponentSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "ComponentSpecificConfiguration"));

		if (this.getTypedComponentSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "TypedComponentSpecificConfiguration"));

		return this.getTypedComponentSpecificConfiguration().validate(context);
	}
}

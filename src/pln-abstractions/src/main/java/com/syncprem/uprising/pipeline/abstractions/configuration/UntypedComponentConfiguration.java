/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.infrastructure.serialization.JsonSerializationStrategyImpl;
import com.syncprem.uprising.pipeline.abstractions.SpecConfComponent;

import java.util.*;

public class UntypedComponentConfiguration extends ComponentConfiguration
{
	public UntypedComponentConfiguration()
	{
		this(new LinkedHashMap<>(), ComponentSpecificConfiguration.class);
	}

	public UntypedComponentConfiguration(Map<String, Object> componentSpecificConfiguration, Class<? extends ComponentSpecificConfiguration> untypedComponentSpecificConfigurationClass)
	{
		if (componentSpecificConfiguration == null)
			throw new ArgumentNullException("componentSpecificConfiguration");

		if (untypedComponentSpecificConfigurationClass == null)
			throw new ArgumentNullException("untypedComponentSpecificConfigurationClass");

		this.componentSpecificConfiguration = componentSpecificConfiguration;
		this.untypedComponentSpecificConfigurationClass = untypedComponentSpecificConfigurationClass;
	}

	private final Map<String, Object> componentSpecificConfiguration;
	private final Class<? extends ComponentSpecificConfiguration> untypedComponentSpecificConfigurationClass;
	private String componentClassName;
	private ComponentSpecificConfiguration untypedComponentSpecificConfiguration;

	public <TComponent extends SpecConfComponent<? extends ComponentSpecificConfiguration>> Class<? extends TComponent> getComponentClass()
	{
		Class<? extends TComponent> clazz;

		clazz = Utils.loadClassByName(this.getComponentClassName());

		return clazz;
	}

	public String getComponentClassName()
	{
		return this.componentClassName;
	}

	public void setComponentClassName(String componentClassName)
	{
		this.componentClassName = componentClassName;
	}

	public Map<String, Object> getComponentSpecificConfiguration()
	{
		return this.componentSpecificConfiguration;
	}

	public ComponentSpecificConfiguration getUntypedComponentSpecificConfiguration()
	{
		this.applyUntypedComponentSpecificConfiguration(); // special case
		return this.untypedComponentSpecificConfiguration;
	}

	public void setUntypedComponentSpecificConfiguration(ComponentSpecificConfiguration untypedComponentSpecificConfiguration)
	{
		this.ensureParentOnSetter(this.untypedComponentSpecificConfiguration, untypedComponentSpecificConfiguration);
		this.untypedComponentSpecificConfiguration = untypedComponentSpecificConfiguration;
	}

	public Class<? extends ComponentSpecificConfiguration> getUntypedComponentSpecificConfigurationClass()
	{
		return this.untypedComponentSpecificConfigurationClass;
	}

	private void applyUntypedComponentSpecificConfiguration(Class<? extends ComponentSpecificConfiguration> clazz)
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (this.getComponentSpecificConfiguration() != null)
		{
			this.setUntypedComponentSpecificConfiguration(JsonSerializationStrategyImpl.getObjectFromJsonMap(this.getComponentSpecificConfiguration(), clazz));
		}
	}

	public final void applyUntypedComponentSpecificConfiguration()
	{
		this.applyUntypedComponentSpecificConfiguration(this.getUntypedComponentSpecificConfigurationClass());
	}

	public void resetUntypedComponentSpecificConfiguration()
	{
		this.getComponentSpecificConfiguration().clear();
		this.setUntypedComponentSpecificConfiguration(null);
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;
		Class<? extends SpecConfComponent<? extends ComponentSpecificConfiguration>> componentClass;
		SpecConfComponent<? extends ComponentSpecificConfiguration> component;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getComponentClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s component class name is required.", context), Severity.ERROR));
		else
		{
			componentClass = this.getComponentClass();

			if (componentClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s component failed to load type from class name.", context), Severity.ERROR));
			else if (!SpecConfComponent.class.isAssignableFrom(componentClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s component loaded an unrecognized type via class name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				component = Utils.newObjectFromClass(componentClass);

				if (component == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s component failed to instantiate type from class name.", context), Severity.ERROR));
				else
				{
					try (SpecConfComponent<? extends ComponentSpecificConfiguration> _component = component) // ???
					{
						Class<? extends ComponentSpecificConfiguration> specificationClass;

						component.setConfiguration(this);
						component.create();

						// "Hey Component, tell me what your Specification (Component SpecificConfiguration derived) class is?"
						specificationClass = component.getSpecificationClass();
						this.applyUntypedComponentSpecificConfiguration(specificationClass);

						//MessageImpl.addRange(messages, this.validateUntypedComponentSpecificConfiguration(context));
						MessageImpl.addRange(messages, _component.getSpecification().validate(String.format("%s", context)));
					}
					catch (Exception ex)
					{
						messages.add(new MessageImpl(Utils.EMPTY_STRING, ex.toString(), Severity.ERROR));
					}
				}
			}
		}

		return messages;
	}

	public final Iterable<Message> validateUntypedComponentSpecificConfiguration(Object context)
	{
		if (this.getComponentSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "ComponentSpecificConfiguration"));

		if (this.getUntypedComponentSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%d'.", "UntypedComponentSpecificConfiguration"));

		return this.getUntypedComponentSpecificConfiguration().validate(context);
	}
}

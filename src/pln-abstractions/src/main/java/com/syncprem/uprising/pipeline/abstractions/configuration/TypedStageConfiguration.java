/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.polyfills.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypedStageConfiguration<TStageSpecificConfiguration extends StageSpecificConfiguration> extends UntypedStageConfiguration
{
	public TypedStageConfiguration(UntypedStageConfiguration stageConfiguration, Class<? extends TStageSpecificConfiguration> typedStageSpecificConfigurationClass)
	{
		this(new HashMap<>(), stageConfiguration, typedStageSpecificConfigurationClass);
	}

	public TypedStageConfiguration(Map<String, Object> stageSpecificConfiguration, UntypedStageConfiguration stageConfiguration, Class<? extends TStageSpecificConfiguration> typedStageSpecificConfigurationClass)
	{
		super(stageSpecificConfiguration, typedStageSpecificConfigurationClass);

		if (stageConfiguration == null)
			throw new ArgumentNullException("stageConfiguration");

		if (typedStageSpecificConfigurationClass == null)
			throw new ArgumentNullException("typedStageSpecificConfigurationClass");

		if (this.getStageSpecificConfiguration() != null &&
				stageConfiguration.getStageSpecificConfiguration() != null)
		{
			final Map<String, Object> otherStageSpecificConfiguration = stageConfiguration.getStageSpecificConfiguration();
			for (Map.Entry<String, Object> entry : otherStageSpecificConfiguration.entrySet())
			{
				if (entry == null)
					continue;

				this.getStageSpecificConfiguration().put(entry.getKey(), entry.getValue());
			}
		}

		this.setStageClassName(stageConfiguration.getStageClassName());
		this.setParent(stageConfiguration.getParent());
		this.setSurround(stageConfiguration.getSurround());

		this.typedStageSpecificConfigurationClass = typedStageSpecificConfigurationClass;
	}

	private final Class<? extends TStageSpecificConfiguration> typedStageSpecificConfigurationClass;
	private TStageSpecificConfiguration typedStageSpecificConfiguration;

	public TStageSpecificConfiguration getTypedStageSpecificConfiguration()
	{
		this.applyTypedStageSpecificConfiguration(); // special case
		return this.typedStageSpecificConfiguration;
	}

	public void setTypedStageSpecificConfiguration(TStageSpecificConfiguration typedStageSpecificConfiguration)
	{
		this.ensureParentOnSetter(this.typedStageSpecificConfiguration, typedStageSpecificConfiguration);
		this.typedStageSpecificConfiguration = typedStageSpecificConfiguration;
	}

	public Class<? extends TStageSpecificConfiguration> getTypedStageSpecificConfigurationClass()
	{
		//typeof(TStageSpecificConfiguration);
		return this.typedStageSpecificConfigurationClass;
	}

	private void applyTypedStageSpecificConfiguration(Class<? extends TStageSpecificConfiguration> clazz)
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (this.getStageSpecificConfiguration() != null)
		{
			TStageSpecificConfiguration temp;

			temp = Utils.getObjectFromJsonMap(this.getStageSpecificConfiguration(), clazz);

			//if (temp == null)
			//temp = Utils.newObjectFromClass(clazz);

			this.setTypedStageSpecificConfiguration(temp);
		}
	}

	public final void applyTypedStageSpecificConfiguration()
	{
		this.applyTypedStageSpecificConfiguration(this.getTypedStageSpecificConfigurationClass());
	}

	public void resetTypedStageSpecificConfiguration()
	{
		this.getStageSpecificConfiguration().clear();
		this.setTypedStageSpecificConfiguration(null);
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		MessageImpl.addRange(messages, super.validate(context));

		if (this.getStageSpecificConfiguration() != null)
			MessageImpl.addRange(messages, this.validateTypedStageSpecificConfiguration(context));

		return messages;
	}

	public final Iterable<Message> validateTypedStageSpecificConfiguration(Object context)
	{
		if (this.getStageSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "StageSpecificConfiguration"));

		if (this.getTypedStageSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "TypedStageSpecificConfiguration"));

		return this.getTypedStageSpecificConfiguration().validate(context);
	}
}

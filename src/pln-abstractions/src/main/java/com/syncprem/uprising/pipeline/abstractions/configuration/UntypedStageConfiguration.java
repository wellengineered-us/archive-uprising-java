/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.infrastructure.serialization.JsonSerializationStrategyImpl;
import com.syncprem.uprising.pipeline.abstractions.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UntypedStageConfiguration extends ComponentConfiguration
{
	public UntypedStageConfiguration()
	{
		this(new HashMap<>(), StageSpecificConfiguration.class);
	}

	public UntypedStageConfiguration(Map<String, Object> stageSpecificConfiguration, Class<? extends StageSpecificConfiguration> untypedStageSpecificConfigurationClass)
	{
		if (stageSpecificConfiguration == null)
			throw new ArgumentNullException("stageSpecificConfiguration");

		if (untypedStageSpecificConfigurationClass == null)
			throw new ArgumentNullException("untypedStageSpecificConfigurationClass");

		this.stageSpecificConfiguration = stageSpecificConfiguration;
		this.untypedStageSpecificConfigurationClass = untypedStageSpecificConfigurationClass;
	}

	private final Map<String, Object> stageSpecificConfiguration;
	private final Class<? extends StageSpecificConfiguration> untypedStageSpecificConfigurationClass;
	private String stageClassName;
	private StageSpecificConfiguration untypedStageSpecificConfiguration;

	@SuppressWarnings("unchecked")
	public <TStage extends Stage<? extends StageSpecificConfiguration>> Class<? extends TStage> getStageClass()
	{
		Class<? extends TStage> clazz;
		Class<? extends Stage<? extends StageSpecificConfiguration>> klazz;

		klazz = Utils.loadClassByName(this.getStageClassName());
		clazz = (Class<? extends TStage>) klazz;

		return clazz;
	}

	public String getStageClassName()
	{
		return this.stageClassName;
	}

	public void setStageClassName(String stageClassName)
	{
		this.stageClassName = stageClassName;
	}

	public Map<String, Object> getStageSpecificConfiguration()
	{
		return this.stageSpecificConfiguration;
	}

	public StageSpecificConfiguration getUntypedStageSpecificConfiguration()
	{
		this.applyUntypedStageSpecificConfiguration(); // special case
		return this.untypedStageSpecificConfiguration;
	}

	public void setUntypedStageSpecificConfiguration(StageSpecificConfiguration untypedStageSpecificConfiguration)
	{
		this.ensureParentOnSetter(this.untypedStageSpecificConfiguration, untypedStageSpecificConfiguration);
		this.untypedStageSpecificConfiguration = untypedStageSpecificConfiguration;
	}

	public Class<? extends StageSpecificConfiguration> getUntypedStageSpecificConfigurationClass()
	{
		return this.untypedStageSpecificConfigurationClass;
	}

	private void applyUntypedStageSpecificConfiguration(Class<? extends StageSpecificConfiguration> clazz)
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (this.getStageSpecificConfiguration() != null)
		{
			this.setUntypedStageSpecificConfiguration(JsonSerializationStrategyImpl.getObjectFromJsonMap(this.getStageSpecificConfiguration(), clazz));
		}
	}

	public final void applyUntypedStageSpecificConfiguration()
	{
		this.applyUntypedStageSpecificConfiguration(this.getUntypedStageSpecificConfigurationClass());
	}

	public void resetUntypedStageSpecificConfiguration()
	{
		this.getStageSpecificConfiguration().clear();
		this.setUntypedStageSpecificConfiguration(null);
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;
		Class<? extends Stage<? extends StageSpecificConfiguration>> stageClass;
		Stage<? extends StageSpecificConfiguration> stage;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getStageClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s stage class name is required.", context), Severity.ERROR));
		else
		{
			stageClass = this.getStageClass();

			if (stageClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s stage failed to load type from class name.", context), Severity.ERROR));
			else if (!Stage.class.isAssignableFrom(stageClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s stage loaded an unrecognized type via class name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				stage = Utils.newObjectFromClass(stageClass);

				if (stage == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s stage failed to instantiate type from class name.", context), Severity.ERROR));
				else
				{
					try (Stage<? extends StageSpecificConfiguration> _stage = stage) // ???
					{
						Class<? extends StageSpecificConfiguration> specificationClass;

						stage.setConfiguration(this);
						stage.create();

						// "Hey Stage, tell me what your Specification (Stage SpecificConfiguration derived) class is?"
						specificationClass = stage.getSpecificationClass();
						this.applyUntypedStageSpecificConfiguration(specificationClass);

						//MessageImpl.addRange(messages, this.validateUntypedStageSpecificConfiguration(context));
						MessageImpl.addRange(messages, _stage.getSpecification().validate(String.format("%s", context)));
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

	public final Iterable<Message> validateUntypedStageSpecificConfiguration(Object context)
	{
		if (this.getStageSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "StageSpecificConfiguration"));

		if (this.getUntypedStageSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Configuration missing: '%d'.", "UntypedStageSpecificConfiguration"));

		return this.getUntypedStageSpecificConfiguration().validate(context);
	}
}

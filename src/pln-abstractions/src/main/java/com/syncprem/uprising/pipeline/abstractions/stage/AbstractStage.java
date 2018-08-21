/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage;

import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.pipeline.abstractions.AbstractComponent;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.TypedStageConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedStageConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public abstract class AbstractStage<TStageSpecificConfiguration extends StageSpecificConfiguration> extends AbstractComponent implements Stage<TStageSpecificConfiguration>
{
	protected AbstractStage()
	{
	}

	private UntypedStageConfiguration configuration;
	private TStageSpecificConfiguration specification;

	@Override
	public final UntypedStageConfiguration getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public final void setConfiguration(UntypedStageConfiguration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public final TStageSpecificConfiguration getSpecification()
	{
		return this.specification;
	}

	@Override
	public final void setSpecification(TStageSpecificConfiguration specification)
	{
		this.specification = specification;
	}

	@Override
	public final Class<? extends TStageSpecificConfiguration> getSpecificationClass()
	{
		Class<? extends TStageSpecificConfiguration> clazz;

		clazz = this.getStageSpecificConfigurationClass(null);

		if (clazz == null)
			throw new InvalidOperationException("Did you forget to return a valid class from an overridden getStageSpecificConfigurationClass(Object reserved)?");

		return clazz;
	}

	protected final void assertValidConfiguration()
	{
		if (this.getConfiguration() == null)
			throw new InvalidOperationException(String.format("Stage configuration missing for stage type '%s'.", this.getClass().getSimpleName()));

		if (this.getConfiguration().getStageSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Untyped stage-specific configuration missing for stage type '%s'.", this.getClass().getSimpleName()));

		if (this.getSpecification() == null)
			throw new InvalidOperationException(String.format("Stage specification missing for stage type '%s'.", this.getClass().getSimpleName()));
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		UntypedStageConfiguration untypedStageConfiguration;
		TypedStageConfiguration<TStageSpecificConfiguration> typedStageConfiguration;

		if (this.isCreated())
			return;

		super.create(creating);

		if (creating)
		{
			untypedStageConfiguration = this.getConfiguration();

			if (untypedStageConfiguration == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "untypedStageConfiguration"));

			if (untypedStageConfiguration.getStageSpecificConfiguration() == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "untypedStageConfiguration.StageSpecificConfiguration"));

			typedStageConfiguration = new TypedStageConfiguration<TStageSpecificConfiguration>(untypedStageConfiguration, this.getSpecificationClass());

			if (typedStageConfiguration.getStageSpecificConfiguration() == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "typedStageConfiguration.StageSpecificConfiguration"));

			this.setSpecification(typedStageConfiguration.getTypedStageSpecificConfiguration());

			this.assertValidConfiguration();
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			this.setConfiguration(null);
			this.setSpecification(null);
		}

		super.dispose(disposing);
	}

	protected abstract Class<? extends TStageSpecificConfiguration> getStageSpecificConfigurationClass(Object reserved);

	@Override
	public final void postExecute(Context context, RecordConfiguration configuration) throws SyncPremException
	{
		try
		{
			this.assertValidConfiguration();
			this.postExecuteInternal(context, configuration);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception;

	@Override
	public final void preExecute(Context context, RecordConfiguration configuration) throws SyncPremException
	{
		try
		{
			this.assertValidConfiguration();
			this.preExecuteInternal(context, configuration);
		}
		catch (Exception ex)
		{
			throw new SyncPremException(ex);
		}
	}

	protected abstract void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception;
}

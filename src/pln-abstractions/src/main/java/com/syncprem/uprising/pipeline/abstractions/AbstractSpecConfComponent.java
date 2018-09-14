/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions;

import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.TypedComponentConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;

public abstract class AbstractSpecConfComponent<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends AbstractComponent
		implements SpecConfComponent<TComponentSpecificConfiguration>
{
	protected AbstractSpecConfComponent()
	{
	}

	private UntypedComponentConfiguration configuration;
	private TComponentSpecificConfiguration specification;

	@Override
	public final UntypedComponentConfiguration getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public final void setConfiguration(UntypedComponentConfiguration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public final TComponentSpecificConfiguration getSpecification()
	{
		return this.specification;
	}

	@Override
	public final void setSpecification(TComponentSpecificConfiguration specification)
	{
		this.specification = specification;
	}

	@Override
	public final Class<? extends TComponentSpecificConfiguration> getSpecificationClass()
	{
		Class<? extends TComponentSpecificConfiguration> clazz;

		clazz = this.getComponentSpecificConfigurationClass(null);

		if (clazz == null)
			throw new InvalidOperationException("Did you forget to return a valid class from an overridden getComponentSpecificConfigurationClass(Object reserved)?");

		return clazz;
	}

	protected final void assertValidConfiguration()
	{
		if (this.getConfiguration() == null)
			throw new InvalidOperationException(String.format("Component configuration missing for component type '%s'.", this.getClass().getSimpleName()));

		if (this.getConfiguration().getComponentSpecificConfiguration() == null)
			throw new InvalidOperationException(String.format("Untyped component-specific configuration missing for component type '%s'.", this.getClass().getSimpleName()));

		if (this.getSpecification() == null)
			throw new InvalidOperationException(String.format("Component specification missing for component type '%s'.", this.getClass().getSimpleName()));
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		UntypedComponentConfiguration untypedComponentConfiguration;
		TypedComponentConfiguration<TComponentSpecificConfiguration> typedComponentConfiguration;

		if (this.isCreated())
			return;

		super.create(creating);

		if (creating)
		{
			untypedComponentConfiguration = this.getConfiguration();

			if (untypedComponentConfiguration == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "untypedComponentConfiguration"));

			if (untypedComponentConfiguration.getComponentSpecificConfiguration() == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "untypedComponentConfiguration.ComponentSpecificConfiguration"));

			typedComponentConfiguration = new TypedComponentConfiguration<TComponentSpecificConfiguration>(untypedComponentConfiguration, this.getSpecificationClass());

			if (typedComponentConfiguration.getComponentSpecificConfiguration() == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "typedComponentConfiguration.ComponentSpecificConfiguration"));

			this.setSpecification(typedComponentConfiguration.getTypedComponentSpecificConfiguration());

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

	protected abstract Class<? extends TComponentSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved);
}

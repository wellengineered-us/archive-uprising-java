/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentConfiguration;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import java.util.ArrayList;
import java.util.List;

public final class MiddlewareBuilderImpl<TComponent extends Component, TConfiguration extends ComponentConfiguration> implements MiddlewareBuilder<TComponent>, MiddlewareBuilderExtensions<TComponent, TConfiguration>
{
	public MiddlewareBuilderImpl()
	{
		this(new ArrayList<>());
	}

	public MiddlewareBuilderImpl(List<MiddlewareChainDelegate<MiddlewareDelegate<TComponent>, MiddlewareDelegate<TComponent>>> components)
	{
		if (components == null)
			throw new ArgumentNullException("components");

		this.components = components;
	}

	private final List<MiddlewareChainDelegate<MiddlewareDelegate<TComponent>, MiddlewareDelegate<TComponent>>> components;

	private List<MiddlewareChainDelegate<MiddlewareDelegate<TComponent>, MiddlewareDelegate<TComponent>>> getComponents()
	{
		return this.components;
	}

	@Override
	public MiddlewareDelegate<TComponent> build() throws Exception
	{
		MiddlewareDelegate<TComponent> transform = (context, configuration, target) -> target; // simply return original target unmodified

		// REVERSE LIST - LIFO order
		for (int i = this.getComponents().size() - 1; i >= 0; i--)
		{
			final MiddlewareChainDelegate<MiddlewareDelegate<TComponent>, MiddlewareDelegate<TComponent>> component = this.getComponents().get(i);
			final MiddlewareDelegate<TComponent> _transform = transform;

			if (component == null)
				continue;

			transform = component.invoke(_transform);
		}

		return transform;
	}

	@Override
	public MiddlewareBuilder<TComponent> from(Class<? extends Middleware<TComponent, TConfiguration>> middlewareClass, TConfiguration middlewareConfiguration) throws Exception
	{
		if (middlewareClass == null)
			throw new ArgumentNullException("middlewareClass");

		if (middlewareConfiguration == null)
			throw new ArgumentNullException("middlewareConfiguration");

		return this.use(next ->
		{
			return (context, configuration, target) ->
			{
				TComponent newTarget;

				if (context == null)
					throw new InvalidOperationException("context");

				if (configuration == null)
					throw new InvalidOperationException("configuration");

				if (target == null)
					throw new InvalidOperationException("target");

				if (middlewareClass == null)
					throw new InvalidOperationException("middlewareClass");

				if (middlewareConfiguration == null)
					throw new InvalidOperationException("middlewareConfiguration");

				try (Middleware<TComponent, TConfiguration> middleware = Utils.newObjectFromClass(middlewareClass))
				{
					if (middleware == null)
						throw new InvalidOperationException("middleware");

					middleware.setConfiguration(middlewareConfiguration);
					middleware.create();

					newTarget = middleware.process(context, configuration, target, next);

					return newTarget;
				}
				catch (Exception ex)
				{
					throw new SyncPremException(ex);
				}
			};
		});
	}

	@Override
	public MiddlewareBuilderImpl<TComponent, TConfiguration> use(MiddlewareChainDelegate<MiddlewareDelegate<TComponent>, MiddlewareDelegate<TComponent>> middleware)
	{
		if (middleware == null)
			throw new ArgumentNullException("middleware");

		this.getComponents().add(middleware);
		return this;
	}

	@Override
	public MiddlewareBuilderImpl<TComponent, TConfiguration> with(Middleware<TComponent, TConfiguration> middleware) throws Exception
	{
		if (middleware == null)
			throw new ArgumentNullException("middleware");

		return this.use(next ->
		{
			return (context, configuration, target) ->
			{
				TComponent newTarget;
				boolean wasCreated;

				if (context == null)
					throw new InvalidOperationException("context");

				if (configuration == null)
					throw new InvalidOperationException("configuration");

				if (target == null)
					throw new InvalidOperationException("target");

				if (middleware == null)
					throw new InvalidOperationException("middleware");

				try
				{
					if (!middleware.isCreated() || middleware.isDisposed())
						;

					newTarget = middleware.process(context, configuration, target, next);

					return newTarget;
				}
				catch (Exception ex)
				{
					throw new SyncPremException(ex);
				}
			};
		});
	}
}

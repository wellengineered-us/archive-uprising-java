/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.middleware;

import com.syncprem.uprising.infrastructure.configuration.ConfigurationObject;
import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

import java.util.ArrayList;
import java.util.List;

public final class MiddlewareBuilderImpl<TData, TComponent extends Creatable & Disposable, TConfiguration extends ConfigurationObject> implements MiddlewareBuilder<TData, TComponent>, MiddlewareBuilderExtensions<TData, TComponent, TConfiguration>
{
	public MiddlewareBuilderImpl()
	{
		this(new ArrayList<>());
	}

	public MiddlewareBuilderImpl(List<MiddlewareChainDelegate<MiddlewareDelegate<TData, TComponent>, MiddlewareDelegate<TData, TComponent>>> components)
	{
		if (components == null)
			throw new ArgumentNullException("components");

		this.components = components;
	}

	private final List<MiddlewareChainDelegate<MiddlewareDelegate<TData, TComponent>, MiddlewareDelegate<TData, TComponent>>> components;

	private List<MiddlewareChainDelegate<MiddlewareDelegate<TData, TComponent>, MiddlewareDelegate<TData, TComponent>>> getComponents()
	{
		return this.components;
	}

	@Override
	public MiddlewareDelegate<TData, TComponent> build() throws SyncPremException
	{
		MiddlewareDelegate<TData, TComponent> transform = (data, target) -> target; // simply return original target unmodified

		// REVERSE LIST - LIFO order
		for (int i = this.getComponents().size() - 1; i >= 0; i--)
		{
			final MiddlewareChainDelegate<MiddlewareDelegate<TData, TComponent>, MiddlewareDelegate<TData, TComponent>> component = this.getComponents().get(i);
			final MiddlewareDelegate<TData, TComponent> _transform = transform;

			if (component == null)
				continue;

			transform = component.invoke(_transform);
		}

		return transform;
	}

	@Override
	public MiddlewareBuilder<TData, TComponent> from(Class<? extends Middleware<TData, TComponent, TConfiguration>> middlewareClass, TConfiguration middlewareConfiguration) throws SyncPremException
	{
		if (middlewareClass == null)
			throw new ArgumentNullException("middlewareClass");

		if (middlewareConfiguration == null)
			throw new ArgumentNullException("middlewareConfiguration");

		return this.use(next ->
		{
			return (data, target) ->
			{
				TComponent newTarget;

				if (data == null)
					throw new InvalidOperationException("data");

				if (target == null)
					throw new InvalidOperationException("target");

				if (middlewareClass == null)
					throw new InvalidOperationException("middlewareClass");

				if (middlewareConfiguration == null)
					throw new InvalidOperationException("middlewareConfiguration");

				try (Middleware<TData, TComponent, TConfiguration> middleware = Utils.newObjectFromClass(middlewareClass))
				{
					if (middleware == null)
						throw new InvalidOperationException("middleware");

					middleware.setConfiguration(middlewareConfiguration);
					middleware.create();

					newTarget = middleware.process(data, target, next);

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
	public MiddlewareBuilderImpl<TData, TComponent, TConfiguration> use(MiddlewareChainDelegate<MiddlewareDelegate<TData, TComponent>, MiddlewareDelegate<TData, TComponent>> middleware)
	{
		if (middleware == null)
			throw new ArgumentNullException("middleware");

		this.getComponents().add(middleware);
		return this;
	}

	@Override
	public MiddlewareBuilderImpl<TData, TComponent, TConfiguration> with(Middleware<TData, TComponent, TConfiguration> middleware) throws SyncPremException
	{
		if (middleware == null)
			throw new ArgumentNullException("middleware");

		return this.use(next ->
		{
			return (data, target) ->
			{
				TComponent newTarget;

				if (data == null)
					throw new InvalidOperationException("data");

				if (target == null)
					throw new InvalidOperationException("target");

				if (middleware == null)
					throw new InvalidOperationException("middleware");

				try
				{
					if (!middleware.isCreated() || middleware.isDisposed())
						;

					newTarget = middleware.process(data, target, next);

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

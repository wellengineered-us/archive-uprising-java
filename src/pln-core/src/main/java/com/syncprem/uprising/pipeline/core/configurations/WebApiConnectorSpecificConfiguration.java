/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;
import com.syncprem.uprising.streamingio.proxywrappers.strategies.CompressionStrategy;
import com.syncprem.uprising.streamingio.restful.HttpMethod;
import com.syncprem.uprising.streamingio.restful.HttpScope;

import java.util.ArrayList;
import java.util.List;

public class WebApiConnectorSpecificConfiguration extends StageSpecificConfiguration
{
	public WebApiConnectorSpecificConfiguration()
	{
	}

	private String compressionStrategyClassName;
	private String endpointUri;
	private HttpMethod httpMethod;
	private HttpScope httpScope;
	private String serializationStrategyClassName;

	public Class<? extends CompressionStrategy> getCompressionStrategyClass()
	{
		return Utils.loadClassByName(this.getCompressionStrategyClassName(), CompressionStrategy.class);
	}

	public String getCompressionStrategyClassName()
	{
		return this.compressionStrategyClassName;
	}

	public void setCompressionStrategyClassName(String compressionStrategyClassName)
	{
		this.compressionStrategyClassName = compressionStrategyClassName;
	}

	public String getEndpointUri()
	{
		return this.endpointUri;
	}

	public void setEndpointUri(String endpointUri)
	{
		this.endpointUri = endpointUri;
	}

	public HttpMethod getHttpMethod()
	{
		return this.httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}

	public HttpScope getHttpScope()
	{
		return this.httpScope;
	}

	public void setHttpScope(HttpScope httpScope)
	{
		this.httpScope = httpScope;
	}

	public Class<? extends SerializationStrategy> getSerializationStrategyClass()
	{
		return Utils.loadClassByName(this.getSerializationStrategyClassName(), SerializationStrategy.class);
	}

	public String getSerializationStrategyClassName()
	{
		return this.serializationStrategyClassName;
	}

	public void setSerializationStrategyClassName(String serializationStrategyClassName)
	{
		this.serializationStrategyClassName = serializationStrategyClassName;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;
		Class<? extends SerializationStrategy> serializationStrategyClass;
		SerializationStrategy serializationStrategy_;

		Class<? extends CompressionStrategy> compressionStrategyClass;
		CompressionStrategy compressionStrategy_;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getEndpointUri()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s adapter endpoint URI is required.", context), Severity.ERROR));

		if (this.getHttpMethod() == null ||
				this.getHttpMethod() == HttpMethod.UNKNOWN)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s adapter HTTP verb is required.", context), Severity.ERROR));

		if (this.getHttpScope() == null ||
				this.getHttpScope() == HttpScope.UNKNOWN)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s adapter HTTP scope is required.", context), Severity.ERROR));

		if (Utils.isNullOrEmptyString(this.getSerializationStrategyClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s driver class name is required.", context), Severity.ERROR));
		else
		{
			serializationStrategyClass = this.getSerializationStrategyClass();

			if (serializationStrategyClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to load driver driver class by name.", context), Severity.ERROR));
			else if (!SerializationStrategy.class.isAssignableFrom(serializationStrategyClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s loaded an unrecognized driver driver class by name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				serializationStrategy_ = Utils.newObjectFromClass(serializationStrategyClass);

				if (serializationStrategy_ == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to instantiate driver driver class by name using default, public constructor.", context), Severity.ERROR));
			}
		}

		if (Utils.isNullOrEmptyString(this.getCompressionStrategyClassName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s compressionStrategy class name is required.", context), Severity.ERROR));
		else
		{
			compressionStrategyClass = this.getCompressionStrategyClass();

			if (compressionStrategyClass == null)
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to load compressionStrategy compressionStrategy class by name.", context), Severity.ERROR));
			else if (!CompressionStrategy.class.isAssignableFrom(compressionStrategyClass))
				messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s loaded an unrecognized compressionStrategy compressionStrategy class by name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				compressionStrategy_ = Utils.newObjectFromClass(compressionStrategyClass);

				if (compressionStrategy_ == null)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s failed to instantiate compressionStrategy compressionStrategy class by name using default, public constructor.", context), Severity.ERROR));
			}
		}

		return messages;
	}
}

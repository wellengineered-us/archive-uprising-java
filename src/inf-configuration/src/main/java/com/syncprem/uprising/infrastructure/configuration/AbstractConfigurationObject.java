/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.configuration;

import com.syncprem.uprising.infrastructure.polyfills.Message;

import java.util.Collections;
import java.util.List;

/**
 * Provides a base for all configuration objects.
 */
public class AbstractConfigurationObject implements ConfigurationObject
{
	protected AbstractConfigurationObject()
	{
		this.items = new ConfigurationObjectCollectionImpl<ConfigurationObject>(this); // cannot pass in "this"
	}

	private final ConfigurationObjectCollectionImpl<ConfigurationObject> items;
	private ConfigurationObject content;
	private ConfigurationObject parent;
	private ConfigurationObjectCollectionImpl<?> surround;

	@Override
	public Iterable<Class<? extends ConfigurationObject>> getAllowedChildClasses()
	{
		List<Class<? extends ConfigurationObject>> clazzes = Collections.emptyList();
		return clazzes;
	}

	@ConfigurationIgnore
	@Override
	public ConfigurationObject getContent()
	{
		return this.content;
	}

	@ConfigurationIgnore
	@Override
	public void setContent(ConfigurationObject content)
	{
		this.ensureParentOnSetter(this.content, content);
		this.content = content;
	}

	@ConfigurationIgnore
	@Override
	public ConfigurationObjectCollectionImpl<ConfigurationObject> getItems()
	{
		return this.items;
	}

	@ConfigurationIgnore
	@Override
	public ConfigurationObject getParent()
	{
		return this.parent;
	}

	@ConfigurationIgnore
	@Override
	public void setParent(ConfigurationObject parent)
	{
		this.parent = parent;
	}

	@ConfigurationIgnore
	@Override
	public ConfigurationObjectCollectionImpl<?> getSurround()
	{
		return this.surround;
	}

	@ConfigurationIgnore
	@Override
	public void setSurround(ConfigurationObjectCollectionImpl<?> surround)
	{
		this.surround = surround;
	}

	/**
	 * Ensures that for any configuration object property, the correct parent instance is set/unset.
	 * Should be called in the setter for all configuration object properties, before assigning the value.
	 * Example:
	 * setThingy(Thingy thingy) { this.ensureParentOnSetter(this.thingy, thingy); this.thingy = thingy; }
	 *
	 * @param oldValueObj The old configuration object value (the backing field).
	 * @param newValueObj The new configuration object value (the local parameter in setter method).
	 */
	protected void ensureParentOnSetter(ConfigurationObject oldValueObj, ConfigurationObject newValueObj)
	{
		if (oldValueObj != null)
		{
			oldValueObj.setSurround(null);
			oldValueObj.setParent(null);
		}

		if (newValueObj != null)
		{
			newValueObj.setSurround(null);
			newValueObj.setParent(this);
		}
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		final List<Message> messages = Collections.emptyList();
		return messages;
	}

	@Override
	public final Iterable<Message> validate()
	{
		return this.validate(null);
	}
}

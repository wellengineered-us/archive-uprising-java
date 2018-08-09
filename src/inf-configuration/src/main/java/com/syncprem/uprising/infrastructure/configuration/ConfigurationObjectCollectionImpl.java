/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.configuration;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ConfigurationObjectCollectionImpl<TItemConfigurationObject extends ConfigurationObject> implements ConfigurationObjectCollection<TItemConfigurationObject>
{
	public ConfigurationObjectCollectionImpl()
	{
		this(new ConfigurationObjectImpl(), new ArrayList<>());
	}

	public ConfigurationObjectCollectionImpl(ConfigurationObject site)
	{
		this(site, new ArrayList<>());
	}

	public ConfigurationObjectCollectionImpl(ConfigurationObject site, Collection<TItemConfigurationObject> collection)
	{
		if (site == null)
			throw new ArgumentNullException("site");

		if (collection == null)
			throw new ArgumentNullException("collection");

		this.site = site;
		this.collection = collection;
	}

	private final Collection<TItemConfigurationObject> collection;
	private final ConfigurationObject site;

	private Collection<TItemConfigurationObject> getCollection()
	{
		return this.collection;
	}

	@ConfigurationIgnore
	@Override
	public ConfigurationObject getSite()
	{
		return this.site;
	}

	@Override
	public boolean isEmpty()
	{
		return this.getCollection().isEmpty();
	}

	@Override
	public boolean add(TItemConfigurationObject item)
	{
		if (item != null)
		{
			item.setSurround(this);
			item.setParent(this.getSite());
		}

		return this.getCollection().add(item);
	}

	@Override
	public boolean addAll(Collection<? extends TItemConfigurationObject> col)
	{
		if (col == null)
		{
			for (TItemConfigurationObject item : col)
			{
				if (item == null)
					continue;

				item.setSurround(null);
				item.setParent(null);
			}
		}

		return this.getCollection().addAll(col);
	}

	@Override
	public void clear()
	{
		for (TItemConfigurationObject item : this.getCollection())
		{
			if (item == null)
				continue;

			item.setSurround(null);
			item.setParent(null);
		}

		this.getCollection().clear();
	}

	@Override
	public boolean contains(Object obj)
	{
		return this.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> col)
	{
		return this.getCollection().containsAll(col);
	}

	@Override
	public Iterator<TItemConfigurationObject> iterator()
	{
		return this.getCollection().iterator();
	}

	@Override
	public boolean remove(Object obj)
	{
		ConfigurationObject item;

		if (obj instanceof ConfigurationObject)
		{
			item = (ConfigurationObject) obj;
			item.setSurround(null);
			item.setParent(null);
		}

		return this.getCollection().remove(obj);
	}

	@Override
	public boolean removeAll(Collection<?> col)
	{
		if (col == null)
		{
			for (Object obj : col)
			{
				ConfigurationObject item;

				if (obj == null)
					continue;

				if (obj instanceof ConfigurationObject)
				{
					item = (ConfigurationObject) obj;
					item.setSurround(null);
					item.setParent(null);
				}
			}
		}

		return this.getCollection().removeAll(col);
	}

	@Override
	public boolean retainAll(Collection<?> col)
	{
		if (col == null)
		{
			for (Object obj : col)
			{
				ConfigurationObject item;

				if (obj == null)
					continue;

				if (obj instanceof ConfigurationObject)
				{
					item = (ConfigurationObject) obj;
					item.setSurround(this);
					item.setParent(this.getSite());
				}
			}
		}

		return this.getCollection().retainAll(col);
	}

	@Override
	public int size()
	{
		return this.getCollection().size();
	}

	@Override
	public Object[] toArray()
	{
		return this.getCollection().toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] array)
	{
		return this.getCollection().toArray(array);
	}

	@Override
	public final Iterable<Message> validateAll(Object context)
	{
		List<Message> messages;
		int index = 0;

		messages = new ArrayList<>();

		for (TItemConfigurationObject item : this.getCollection())
		{
			final String value = context == null ? String.format("%s", index++) : String.format("%s[%s]", context, index++);
			Iterable<Message> iterable;

			if (item == null)
				continue;

			iterable = item.validate(value);
			MessageImpl.addRange(messages, iterable);
		}

		return messages;
	}

	@Override
	public final Iterable<Message> validateAll()
	{
		return this.validateAll(null);
	}
}

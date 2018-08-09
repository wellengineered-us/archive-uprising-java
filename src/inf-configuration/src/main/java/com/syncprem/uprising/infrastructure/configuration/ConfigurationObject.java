/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.configuration;

import com.syncprem.uprising.infrastructure.polyfills.Validatable;

/**
 * Represents an configuration object and it's "schema".
 */
public interface ConfigurationObject extends Validatable
{
	/**
	 * Gets an iterable of allowed child configuration object classes.
	 *
	 * @return
	 */
	Iterable<Class<? extends ConfigurationObject>> getAllowedChildClasses();

	/**
	 * Gets the optional single configuration content object.
	 *
	 * @return
	 */
	ConfigurationObject getContent();

	/**
	 * Sets the optional single configuration content object.
	 *
	 * @param content
	 */
	void setContent(ConfigurationObject content);

	/**
	 * Gets a list of configuration object items.
	 *
	 * @return
	 */
	ConfigurationObjectCollectionImpl<ConfigurationObject> getItems();

	/**
	 * Gets the parent configuration object or null if this is the configuration root.
	 *
	 * @return
	 */
	ConfigurationObject getParent();

	/**
	 * Sets the parent configuration object or null if this is the configuration root.
	 *
	 * @param parent
	 */
	void setParent(ConfigurationObject parent);

	/**
	 * Gets the surrounding configuration object or null if this is not surrounded (in a collection).
	 *
	 * @return
	 */
	ConfigurationObjectCollectionImpl<?> getSurround();

	/**
	 * Sets the surrounding configuration object or null if this is not surrounded (in a collection).
	 *
	 * @param surround
	 */
	void setSurround(ConfigurationObjectCollectionImpl<?> surround);
}

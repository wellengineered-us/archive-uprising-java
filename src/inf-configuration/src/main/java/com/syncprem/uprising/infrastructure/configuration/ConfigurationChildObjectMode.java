/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.configuration;

/**
 * Specifies the configuration child object type (applicable only to those which are well-known via properties with implicit or explicit mapping annotations).
 */
public enum ConfigurationChildObjectMode
{
	/**
	 * This configuration object is not allowed to have any child objects. This is the default.
	 */
	NONE,

	/**
	 * This configuration object can have ONE non-well-known child object. Use the getContent() getter to access the possibly null value.
	 */
	CONTENT,

	/**
	 * This configuration object can have MANY non-well-known child objects. Use the Items property to access the possibly empty list of values.
	 */
	VALUE
}

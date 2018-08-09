/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.configuration;

public interface ConfigurationValueObject<T> extends ConfigurationObject
{
	String getName();

	void setName(String name);

	T getValue();

	void setValue(T value);
}

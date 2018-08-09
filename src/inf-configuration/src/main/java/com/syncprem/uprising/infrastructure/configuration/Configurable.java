/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.configuration;

public interface Configurable<TConfiguration extends ConfigurationObject>
{
	TConfiguration getConfiguration();

	void setConfiguration(TConfiguration configuration);
}

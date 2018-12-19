/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public interface Creatable
{
	boolean isCreated();

	void create() throws Exception;
}

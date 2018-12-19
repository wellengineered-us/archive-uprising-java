/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public interface Message
{
	String getCategory();

	String getDescription();

	Severity getSeverity();
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public interface Validatable
{
	Iterable<Message> validate();

	Iterable<Message> validate(Object context);
}

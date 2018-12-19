/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.util.UUID;

public interface Component extends Creatable, Disposable
{
	UUID getComponentId();

	boolean isAsync();

	boolean isReusable();
}

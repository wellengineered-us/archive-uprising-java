/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.Component;

public interface Stream extends Component, LifecycleIterator<Record>
{
}

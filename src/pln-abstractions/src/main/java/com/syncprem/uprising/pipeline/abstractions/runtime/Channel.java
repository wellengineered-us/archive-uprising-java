/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.pipeline.abstractions.Component;

public interface Channel extends Component
{
	Stream getRecords();

	default Stream getStream()
	{
		return this.getRecords();
	}
}

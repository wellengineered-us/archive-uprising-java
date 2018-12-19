/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.pipeline.abstractions.runtime.AbstractChannel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Stream;

public final class ChannelImpl extends AbstractChannel
{
	public ChannelImpl(Stream stream)
	{
		super(stream);
	}
}

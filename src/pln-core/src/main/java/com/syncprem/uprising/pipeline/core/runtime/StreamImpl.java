/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.runtime;

import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.pipeline.abstractions.runtime.AbstractStream;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;

public final class StreamImpl extends AbstractStream
{
	public StreamImpl(LifecycleIterator<Record> records)
	{
		super(records);
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public final class ProcessorClosure
{
	private ProcessorClosure(ProcessToNextDelegate processToNext, ProcessDelegate next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		this.processToNext = processToNext;
		this.next = next;
	}

	private final ProcessDelegate next;
	private final ProcessToNextDelegate processToNext;

	private ProcessDelegate getNext()
	{
		return this.next;
	}

	private ProcessToNextDelegate getProcessToNext()
	{
		return this.processToNext;
	}

	public static ProcessDelegate getMiddlewareChain(ProcessToNextDelegate processToNext, ProcessDelegate next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		return new ProcessorClosure(processToNext, next)::transform;
	}

	private Channel transform(Context context, RecordConfiguration configuration, Channel channel) throws SyncPremException
	{
		System.out.println("voo doo!");
		return this.getProcessToNext().invoke(context, configuration, channel, this.getNext());
	}
}

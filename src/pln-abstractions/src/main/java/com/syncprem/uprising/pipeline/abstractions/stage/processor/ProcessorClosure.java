/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.stage.processor;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.abstractions.Component;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.primitives.SyncPremException;

public final class ProcessorClosure<TTarget extends Component>
{
	private ProcessorClosure(ProcessToNextDelegate<TTarget> processToNext, ProcessDelegate<TTarget> next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		this.processToNext = processToNext;
		this.next = next;
	}

	private final ProcessDelegate<TTarget> next;
	private final ProcessToNextDelegate<TTarget> processToNext;

	private ProcessDelegate<TTarget> getNext()
	{
		return this.next;
	}

	private ProcessToNextDelegate<TTarget> getProcessToNext()
	{
		return this.processToNext;
	}

	public static <TTarget extends Component> ProcessDelegate<TTarget> getMiddlewareChain(ProcessToNextDelegate<TTarget> processToNext, ProcessDelegate<TTarget> next)
	{
		if (processToNext == null)
			throw new ArgumentNullException("processToNext");

		if (next == null)
			throw new ArgumentNullException("next");

		return new ProcessorClosure<>(processToNext, next)::transform;
	}

	private TTarget transform(Context context, RecordConfiguration configuration, TTarget target) throws SyncPremException
	{
		System.out.println("voo doo!");
		return this.getProcessToNext().invoke(context, configuration, target, this.getNext());
	}
}

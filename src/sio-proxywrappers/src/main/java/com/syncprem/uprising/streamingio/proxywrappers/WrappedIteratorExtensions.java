/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import com.syncprem.uprising.infrastructure.polyfills.*;

import java.util.Iterator;

public final class WrappedIteratorExtensions
{
	private static final int DEFAULT_PROCESSING_CALLBACK_PUNCTUATE_MODULO = 100000;

	public static <T> long forceIteration(Iterator<T> iterator) throws Exception
	{
		return forceIteration(iterator, null);
	}

	public static <T> long forceIteration(Iterator<T> iterator, ItemCallback<T, Long> itemCallback) throws Exception
	{
		long recordIndex;

		if (iterator == null)
			throw new ArgumentNullException("iterator");

		try
		{
			recordIndex = -1;
			while (iterator.hasNext())
			{
				final T item = iterator.next();

				++recordIndex;
				if (itemCallback != null)
					itemCallback.onItem(recordIndex, item);
			}

			return recordIndex + 1; // record count NOT index
		}
		finally
		{
			if (!Utils.maybeDispose(iterator))
				Utils.hitWithBrick(iterator);
		}
	}

	public static <T> LifecycleIterator<T> getWrappedIterator(Iterator<T> iterator, String sourceLabel,
															  ItemCallback<T, T> itemCallback,
															  ProcessingCallback processingCallback)
	{
		return new WrappedIteratorImpl<T, T>(DEFAULT_PROCESSING_CALLBACK_PUNCTUATE_MODULO, sourceLabel, itemCallback, processingCallback, iterator);
	}

	public static <T> LifecycleIterator<T> getWrappedIterator(Iterator<T> iterator, ItemCallback<T, T> itemCallback)
	{
		return getWrappedIterator(iterator, Utils.EMPTY_STRING, itemCallback, (punctuateModulo, sourceLabel, itemIndex, isCompleted, rollingTiming) ->
		{
			// do nothing
		});
	}

	public static <T> LifecycleIterator<T> toLifecycleIterator(Iterator<T> iterator)
	{
		return new AbstractForEachYieldIterator<T, T>(iterator, (index, item) -> item)
		{
		};
	}
}

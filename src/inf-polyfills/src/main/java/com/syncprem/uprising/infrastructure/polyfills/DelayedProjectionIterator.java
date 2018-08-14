/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.Iterator;

public final class DelayedProjectionIterator<TInputItem, TOutputItem> extends AbstractForEachYieldIterator<TInputItem, TOutputItem>
{
	public DelayedProjectionIterator(Iterator<TInputItem> baseIterator, ItemCallback<TInputItem, TOutputItem> itemCallback)
	{
		this(ITERATOR_BEFORE_STATE, baseIterator, itemCallback);
	}

	public DelayedProjectionIterator(int state, Iterator<TInputItem> baseIterator, ItemCallback<TInputItem, TOutputItem> itemCallback)
	{
		super(state, baseIterator, itemCallback);
	}

	@Override
	protected Iterator<TOutputItem> newIterator(int state)
	{
		return new DelayedProjectionIterator<TInputItem, TOutputItem>(state, this.getBaseIterator(), this.getItemCallback());
	}
}

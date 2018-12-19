/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.Iterator;

public abstract class AbstractForEachYieldIterator<TInputItem, TOutputItem> extends AbstractYieldIterator<TOutputItem>
{
	protected AbstractForEachYieldIterator(Iterator<TInputItem> baseIterator, ItemCallback<TInputItem, TOutputItem> itemCallback)
	{
		this(ITERATOR_BEFORE_STATE, baseIterator, itemCallback);
	}

	protected AbstractForEachYieldIterator(int machineState, Iterator<TInputItem> baseIterator, ItemCallback<TInputItem, TOutputItem> itemCallback)
	{
		super(machineState);

		if (baseIterator == null)
			throw new ArgumentNullException("baseIterator");

		if (itemCallback == null)
			throw new ArgumentNullException("itemCallback");

		this.baseIterator = baseIterator;
		this.itemCallback = itemCallback;
	}

	private final Iterator<TInputItem> baseIterator;
	private final ItemCallback<TInputItem, TOutputItem> itemCallback;

	protected Iterator<TInputItem> getBaseIterator()
	{
		return this.baseIterator;
	}

	protected ItemCallback<TInputItem, TOutputItem> getItemCallback()
	{
		return this.itemCallback;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			if (!Utils.maybeCreate(this.getBaseIterator()))
				Utils.nop();
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			if (!Utils.maybeDispose(this.getBaseIterator()))
				Utils.hitWithBrick(this.getBaseIterator());
		}
	}

	@Override
	protected Iterator<TOutputItem> newIterator(int state)
	{
		// simple default implementation
		return this;
	}

	@Override
	protected boolean onTryYield(TryOut<TOutputItem> value) throws Exception
	{
		TInputItem oldItem;
		TOutputItem newItem;
		boolean hasNext;

		if (value == null)
			throw new ArgumentNullException("value");

		hasNext = this.getBaseIterator().hasNext();

		if (!hasNext)
			return false;

		oldItem = this.getBaseIterator().next();
		newItem = this.getItemCallback().onItem(this.getItemIndex() + 1 /* tentative index */, oldItem);

		value.setValue(newItem);
		return true;
	}

	@Override
	protected void onYieldComplete() throws Exception
	{
		// do nothing
	}

	@Override
	protected void onYieldFault(Exception ex)
	{
		// do nothing
	}

	@Override
	protected void onYieldResume() throws Exception
	{
		// do nothing
	}

	@Override
	protected void onYieldReturn(TOutputItem value) throws Exception
	{
		// do nothing
	}

	@Override
	protected void onYieldStart() throws Exception
	{
		// do nothing
	}
}

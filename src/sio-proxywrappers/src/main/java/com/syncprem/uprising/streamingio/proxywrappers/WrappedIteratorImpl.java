/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import com.syncprem.uprising.infrastructure.polyfills.AbstractForEachYieldIterator;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.ItemCallback;
import com.syncprem.uprising.infrastructure.polyfills.ProcessingCallback;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public final class WrappedIteratorImpl<TInputItem, TOutputItem> extends AbstractForEachYieldIterator<TInputItem, TOutputItem>
{
	public WrappedIteratorImpl(long punctuateModulo, String sourceLabel, ItemCallback<TInputItem, TOutputItem> itemCallback, ProcessingCallback processingCallback, Iterator<TInputItem> baseIterator)
	{
		this(ITERATOR_BEFORE_STATE, punctuateModulo, sourceLabel, itemCallback, processingCallback, baseIterator);
	}

	public WrappedIteratorImpl(int state, long punctuateModulo, String sourceLabel, ItemCallback<TInputItem, TOutputItem> itemCallback, ProcessingCallback processingCallback, Iterator<TInputItem> baseIterator)
	{
		super(state, baseIterator, itemCallback);

		if (sourceLabel == null)
			throw new ArgumentNullException("sourceLabel");

		if (processingCallback == null)
			throw new ArgumentNullException("processingCallback");

		this.punctuateModulo = punctuateModulo;
		this.sourceLabel = sourceLabel;
		this.processingCallback = processingCallback;
	}

	public static final long DEFAULT_INDEX = -1;
	private final ProcessingCallback processingCallback;
	private final long punctuateModulo;
	private final String sourceLabel;
	private Instant endInstant;
	private Instant startInstant;

	public Instant getEndInstant()
	{
		return this.endInstant;
	}

	private void setEndInstant(Instant endInstant)
	{
		this.endInstant = endInstant;
	}

	private ProcessingCallback getProcessingCallback()
	{
		return this.processingCallback;
	}

	public long getPunctuateModulo()
	{
		return this.punctuateModulo;
	}

	public double getRollingTiming()
	{
		Instant effectiveEndInstant;

		if (this.getStartInstant() == null)
			return Double.NaN;

		if (this.getEndInstant() == null)
			effectiveEndInstant = Instant.now();
		else
			effectiveEndInstant = this.getEndInstant();

		return ChronoUnit.MILLIS.between(this.getStartInstant(), effectiveEndInstant) / 1000.0;
	}

	public String getSourceLabel()
	{
		return this.sourceLabel;
	}

	public Instant getStartInstant()
	{
		return this.startInstant;
	}

	private void setStartInstant(Instant startInstant)
	{
		this.startInstant = startInstant;
	}

	@Override
	protected Iterator<TOutputItem> newIterator(int state)
	{
		return new WrappedIteratorImpl<TInputItem, TOutputItem>(state, this.getPunctuateModulo(), this.getSourceLabel(), this.getItemCallback(), this.getProcessingCallback(), this.getBaseIterator());
	}

	@Override
	protected void onYieldComplete() throws Exception
	{
		super.onYieldComplete();
		this.setEndInstant(Instant.now());

		// post-proc
		this.getProcessingCallback().onProgress(this.getPunctuateModulo(),
				this.getSourceLabel(), this.getItemIndex(), true, this.getRollingTiming());
	}

	@Override
	protected void onYieldReturn(TOutputItem value) throws Exception
	{
		super.onYieldReturn(value);

		if (((this.getItemIndex() + 1) % this.getPunctuateModulo()) == 0)
			this.getProcessingCallback().onProgress(this.getPunctuateModulo(),
					this.getSourceLabel(), this.getItemIndex(), false, this.getRollingTiming());
	}

	@Override
	protected void onYieldStart() throws Exception
	{
		super.onYieldStart();
		this.setStartInstant(Instant.now());

		this.getProcessingCallback().onProgress(this.getPunctuateModulo(),
				this.getSourceLabel(), DEFAULT_INDEX, false, this.getRollingTiming());
	}
}

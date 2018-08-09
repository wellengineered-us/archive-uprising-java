/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.lined;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.NotSupportedException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.textual.AbstractTextualWriter;

import java.io.IOException;
import java.io.Writer;

public class LinedTextualWriterImpl extends AbstractTextualWriter<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>
		implements LinedTextualWriter<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>
{
	public LinedTextualWriterImpl(Writer baseWriter, LinedTextualSpec<LinedTextualFieldSpec> linedTextualSpec)
	{
		super(baseWriter, linedTextualSpec);

		if (linedTextualSpec == null)
			throw new ArgumentNullException("linedTextualSpec");
	}

	private boolean wasFooterRecordWritten;
	private boolean wasHeaderRecordWritten;

	private void setWasFooterRecordWritten(boolean wasFooterRecordWritten)
	{
		this.wasFooterRecordWritten = wasFooterRecordWritten;
	}

	private void setWasHeaderRecordWritten(boolean wasHeaderRecordWritten)
	{
		this.wasHeaderRecordWritten = wasHeaderRecordWritten;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			// do nothing
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			if (this.getBaseWriter() != null)
				this.getBaseWriter().close();
		}
	}

	private boolean wasFooterRecordWritten()
	{
		return this.wasFooterRecordWritten;
	}

	private boolean wasHeaderRecordWritten()
	{
		return this.wasHeaderRecordWritten;
	}

	@Override
	public void writeFooterRecords(Iterable<LinedTextualFieldSpec> footers, LifecycleIterator<Payload> records) throws IOException
	{
		if (footers == null)
			throw new ArgumentNullException("footers");

		if (records == null)
			throw new ArgumentNullException("records");

		throw new NotSupportedException(String.format("Cannot write footer records (from fields) in this version."));
	}

	@Override
	public void writeHeaderFields(Iterable<LinedTextualFieldSpec> headers) throws IOException
	{
		if (headers == null)
			throw new ArgumentNullException("headers");

		throw new NotSupportedException(String.format("Cannot write header records in this version."));
	}

	@Override
	public void writeRecords(LifecycleIterator<Payload> records) throws IOException
	{
		if (records == null)
			throw new ArgumentNullException("records");

		long recordIndex = -1;
		while (records.hasNext())
		{
			final Payload record = records.next();
			Object rawFieldValue;
			String safeFieldValue;

			if (record == null)
				continue;

			rawFieldValue = record;
			safeFieldValue = String.format("[%s] %s", ++recordIndex, Utils.toStringSafe(rawFieldValue));

			this.getBaseWriter().write(safeFieldValue);

			if (!Utils.isNullOrEmptyString(this.getTextualSpec().getRecordDelimiter()))
				this.getBaseWriter().write(this.getTextualSpec().getRecordDelimiter());
			else
				this.getBaseWriter().write(System.lineSeparator());
		}
	}
}

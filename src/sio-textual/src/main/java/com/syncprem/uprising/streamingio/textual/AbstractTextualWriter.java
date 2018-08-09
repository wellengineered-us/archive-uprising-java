/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.streamingio.primitives.Payload;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractTextualWriter<TTextualFieldSpec extends TextualFieldSpec,
		TTextualSpec extends TextualSpec<? extends TTextualFieldSpec>>
		extends AbstractLifecycle<Exception, Exception> implements TextualWriter<TTextualFieldSpec, TTextualSpec>
{
	protected AbstractTextualWriter(Writer baseWriter, TTextualSpec textualSpec)
	{
		if (baseWriter == null)
			throw new ArgumentNullException("baseWriter");

		if (textualSpec == null)
			throw new ArgumentNullException("textualSpec");

		this.baseWriter = baseWriter;
		this.textualSpec = textualSpec;
	}

	private final Writer baseWriter;
	private final TTextualSpec textualSpec;

	@Override
	public Writer getBaseWriter()
	{
		return this.baseWriter;
	}

	@Override
	public TTextualSpec getTextualSpec()
	{
		return this.textualSpec;
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

	@Override
	public void flush() throws IOException
	{
		if (this.getBaseWriter() != null)
			this.getBaseWriter().flush();
	}

	@Override
	public abstract void writeFooterRecords(Iterable<TTextualFieldSpec> footers, LifecycleIterator<Payload> records) throws IOException;

	@Override
	public abstract void writeHeaderFields(Iterable<TTextualFieldSpec> headers) throws IOException;

	@Override
	public abstract void writeRecords(LifecycleIterator<Payload> records) throws IOException;
}

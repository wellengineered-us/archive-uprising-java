/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;

import java.io.IOException;
import java.io.Reader;

public abstract class AbstractTextualReader<TTextualFieldSpec extends TextualFieldSpec,
		TTextualSpec extends TextualSpec<? extends TTextualFieldSpec>>
		extends AbstractLifecycle<Exception, Exception> implements TextualReader<TTextualFieldSpec, TTextualSpec>
{
	protected AbstractTextualReader(Reader baseReader, TTextualSpec textualSpec)
	{
		if (baseReader == null)
			throw new ArgumentNullException("baseReader");

		if (textualSpec == null)
			throw new ArgumentNullException("textualSpec");

		this.baseReader = baseReader;
		this.textualSpec = textualSpec;
	}

	private final Reader baseReader;
	private final TTextualSpec textualSpec;

	@Override
	public Reader getBaseReader() throws IOException
	{
		return this.baseReader;
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
			if (this.getBaseReader() != null)
				this.getBaseReader().close();
		}
	}

	@Override
	public abstract LifecycleIterator<TextualStreamingRecord> readFooterRecords(Iterable<TTextualFieldSpec> footers) throws IOException;

	@Override
	public abstract LifecycleIterator<? extends TTextualFieldSpec> readHeaderFields() throws IOException;

	@Override
	public abstract LifecycleIterator<TextualStreamingRecord> readRecords() throws IOException;
}

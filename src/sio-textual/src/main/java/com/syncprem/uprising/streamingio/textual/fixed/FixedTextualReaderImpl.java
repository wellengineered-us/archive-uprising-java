/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.NotImplementedException;
import com.syncprem.uprising.streamingio.proxywrappers.imports.ExtendedBufferedReader;
import com.syncprem.uprising.streamingio.textual.AbstractTextualReader;
import com.syncprem.uprising.streamingio.textual.TextualStreamingRecord;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualReaderImpl;

import java.io.IOException;
import java.io.Reader;

public class FixedTextualReaderImpl extends AbstractTextualReader<FixedTextualFieldSpec, FixedTextualSpec<FixedTextualFieldSpec>>
		implements FixedTextualReader<FixedTextualFieldSpec, FixedTextualSpec<FixedTextualFieldSpec>>
{
	public FixedTextualReaderImpl(Reader baseReader, FixedTextualSpec<FixedTextualFieldSpec> fixedTextualSpec)
	{
		super(baseReader, fixedTextualSpec);

		if (fixedTextualSpec == null)
			throw new ArgumentNullException("fixedTextualSpec");

		this.bufferedReader = new ExtendedBufferedReader(baseReader);
	}

	private final ExtendedBufferedReader bufferedReader;
	private final LinedTextualReaderImpl.LEXER_STATE lexerState = new LinedTextualReaderImpl.LEXER_STATE();

	private ExtendedBufferedReader getBufferedReader()
	{
		return this.bufferedReader;
	}

	@Override
	public LifecycleIterator<TextualStreamingRecord> readFooterRecords(Iterable<FixedTextualFieldSpec> footers) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public LifecycleIterator<? extends FixedTextualFieldSpec> readHeaderFields() throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public LifecycleIterator<TextualStreamingRecord> readRecords() throws IOException
	{
		throw new NotImplementedException();
	}
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.delimited;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.NotImplementedException;
import com.syncprem.uprising.streamingio.proxywrappers.imports.ExtendedBufferedReader;
import com.syncprem.uprising.streamingio.textual.AbstractTextualReader;
import com.syncprem.uprising.streamingio.textual.TextualStreamingRecord;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualReaderImpl;

import java.io.IOException;
import java.io.Reader;

public class DelimitedTextualReaderImpl extends AbstractTextualReader<DelimitedTextualFieldSpec, DelimitedTextualSpec<DelimitedTextualFieldSpec>>
		implements DelimitedTextualReader<DelimitedTextualFieldSpec, DelimitedTextualSpec<DelimitedTextualFieldSpec>>
{
	public DelimitedTextualReaderImpl(Reader baseReader, DelimitedTextualSpec<DelimitedTextualFieldSpec> delimitedTextualSpec)
	{
		super(baseReader, delimitedTextualSpec);

		if (delimitedTextualSpec == null)
			throw new ArgumentNullException("delimitedTextualSpec");

		this.bufferedReader = new ExtendedBufferedReader(baseReader);
	}

	private final ExtendedBufferedReader bufferedReader;
	private final LinedTextualReaderImpl.LEXER_STATE lexerState = new LinedTextualReaderImpl.LEXER_STATE();

	private ExtendedBufferedReader getBufferedReader()
	{
		return this.bufferedReader;
	}

	@Override
	public LifecycleIterator<TextualStreamingRecord> readFooterRecords(Iterable<DelimitedTextualFieldSpec> footers) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public LifecycleIterator<? extends DelimitedTextualFieldSpec> readHeaderFields() throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public LifecycleIterator<TextualStreamingRecord> readRecords() throws IOException
	{
		throw new NotImplementedException();
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.lined;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;
import com.syncprem.uprising.streamingio.proxywrappers.imports.ExtendedBufferedReader;
import com.syncprem.uprising.streamingio.textual.AbstractTextualReader;
import com.syncprem.uprising.streamingio.textual.TextualStreamingRecord;
import com.syncprem.uprising.streamingio.textual.TextualStreamingRecordImpl;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import static com.syncprem.uprising.infrastructure.polyfills.LeakDetector.*;

public class LinedTextualReaderImpl extends AbstractTextualReader<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>
		implements LinedTextualReader<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>
{
	public LinedTextualReaderImpl(Reader baseReader, LinedTextualSpec<LinedTextualFieldSpec> linedTextualSpec)
	{
		super(baseReader, linedTextualSpec);

		if (linedTextualSpec == null)
			throw new ArgumentNullException("linedTextualSpec");

		this.bufferedReader = new ExtendedBufferedReader(baseReader);
	}

	private final ExtendedBufferedReader bufferedReader;
	private final LEXER_STATE lexerState = new LEXER_STATE();

	private ExtendedBufferedReader getBufferedReader()
	{
		return this.bufferedReader;
	}

	private LEXER_STATE getLexerState()
	{
		return this.lexerState;
	}

	@Override
	public LifecycleIterator<TextualStreamingRecord> readFooterRecords(Iterable<LinedTextualFieldSpec> footers) throws IOException
	{
		if (footers == null)
			throw new ArgumentNullException("footers");

		throw new NotSupportedException(String.format("Cannot read footer records (from fields) in this version."));
	}

	@Override
	public LifecycleIterator<? extends LinedTextualFieldSpec> readHeaderFields() throws IOException
	{
		final Iterable<? extends LinedTextualFieldSpec> specs = Collections.emptyList();
		final Iterator<? extends LinedTextualFieldSpec> iterator = specs.iterator();

		return WrappedIteratorExtensions.getWrappedIterator(iterator, (index, item) -> item);
	}

	@Override
	public LifecycleIterator<TextualStreamingRecord> readRecords() throws IOException
	{
		LifecycleIterator<TextualStreamingRecord> iterator;

		UUID __ = __enter();

		// Note that THE xxxREADER's WILL BE DISPOSED UPON ITERATION BY DESIGN...
		iterator = new AbstractYieldIterator<TextualStreamingRecord>()
		{
			@Override
			protected void create(boolean creating) throws Exception
			{
				if (creating)
				{
					// do nothing
				}
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				if (disposing)
				{
					if (LinedTextualReaderImpl.this.getBufferedReader() != null)
						LinedTextualReaderImpl.this.getBufferedReader().close();

					if (LinedTextualReaderImpl.this.getBaseReader() != null)
						LinedTextualReaderImpl.this.getBaseReader().close();
				}
			}

			@Override
			protected Iterator<TextualStreamingRecord> newIterator(int state)
			{
				// do nothing
				return this;
			}

			@Override
			protected boolean onTryYield(TryOut<TextualStreamingRecord> value) throws Exception
			{
				String line;
				TextualStreamingRecordImpl record;

				if (value == null)
					throw new ArgumentNullException("value");

				if (LinedTextualReaderImpl.this.getLexerState().isEOF)
					return false;

				final long lineNumber = LinedTextualReaderImpl.this.getBufferedReader().getCurrentLineNumber();
				final long charPosition = LinedTextualReaderImpl.this.getBufferedReader().getPosition();

				line = LinedTextualReaderImpl.this.getBufferedReader().readLine();

				if (line == null)
					return false;

				record = new TextualStreamingRecordImpl(++LinedTextualReaderImpl.this.getLexerState().recordIndex,
						lineNumber - 1, charPosition - 1);

				++LinedTextualReaderImpl.this.getLexerState().fieldIndex;
				record.put(Utils.EMPTY_STRING, line);

				value.setValue(record);
				return true;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{
				LinedTextualReaderImpl.this.getLexerState().term(); // set terminal state
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
			protected void onYieldReturn(TextualStreamingRecord value) throws Exception
			{
				// do nothing
			}

			@Override
			protected void onYieldStart() throws Exception
			{
				LinedTextualReaderImpl.this.getLexerState().init(); // set initial state
				LinedTextualReaderImpl.this.getTextualSpec().assertValid();
			}
		};

		__watching(__, iterator);
		__leave(__);

		return iterator;
	}

	public static class LEXER_STATE
	{
		public long characterIndex;
		public long fieldIndex;
		public boolean isEOF;
		public long lineIndex;
		public TextualStreamingRecord record;
		public long recordIndex;

		public void init()
		{
			final long DEFAULT_INDEX = -1;

			this.record = new TextualStreamingRecordImpl();
			this.recordIndex = DEFAULT_INDEX;
			this.fieldIndex = DEFAULT_INDEX;

			this.characterIndex = DEFAULT_INDEX;
			this.lineIndex = DEFAULT_INDEX;
			this.isEOF = false;
		}

		public void term()
		{
			this.record = null;
			this.isEOF = true;
		}
	}
}

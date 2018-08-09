/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class WrappedReaderImpl extends FilterReader
{
	public WrappedReaderImpl(Reader baseReader)
	{
		super(baseReader);
	}

	private Reader getBaseReader()
	{
		return super.in;
	}

	@Override
	public void close() throws IOException
	{
		super.close();
	}

	@Override
	public void mark(int readAheadLimit) throws IOException
	{
		super.mark(readAheadLimit);
	}

	@Override
	public boolean markSupported()
	{
		return super.markSupported();
	}

	@Override
	public int read(CharBuffer target) throws IOException
	{
		return super.read(target);
	}

	@Override
	public int read() throws IOException
	{
		return super.read();
	}

	@Override
	public int read(char[] cbuf) throws IOException
	{
		return super.read(cbuf);
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException
	{
		return super.read(cbuf, off, len);
	}

	@Override
	public boolean ready() throws IOException
	{
		return super.ready();
	}

	@Override
	public void reset() throws IOException
	{
		super.reset();
	}

	@Override
	public long skip(long n) throws IOException
	{
		return super.skip(n);
	}
}

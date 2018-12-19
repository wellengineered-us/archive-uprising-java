/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WrappedInputStreamImpl extends FilterInputStream
{
	public WrappedInputStreamImpl(InputStream baseInputStream)
	{
		super(baseInputStream);
	}

	private InputStream getBaseInputStream()
	{
		return super.in;
	}

	@Override
	public int available() throws IOException
	{
		return super.available();
	}

	@Override
	public void close() throws IOException
	{
		super.close();
	}

	@Override
	public synchronized void mark(int readlimit)
	{
		super.mark(readlimit);
	}

	@Override
	public boolean markSupported()
	{
		return super.markSupported();
	}

	@Override
	public int read() throws IOException
	{
		return super.read();
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		return super.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return super.read(b, off, len);
	}

	@Override
	public byte[] readAllBytes() throws IOException
	{
		return super.readAllBytes();
	}

	@Override
	public int readNBytes(byte[] b, int off, int len) throws IOException
	{
		return super.readNBytes(b, off, len);
	}

	@Override
	public synchronized void reset() throws IOException
	{
		super.reset();
	}

	@Override
	public long skip(long n) throws IOException
	{
		return super.skip(n);
	}

	@Override
	public long transferTo(OutputStream out) throws IOException
	{
		return super.transferTo(out);
	}
}

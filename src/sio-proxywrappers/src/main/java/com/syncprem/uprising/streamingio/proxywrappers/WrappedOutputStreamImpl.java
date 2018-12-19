/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WrappedOutputStreamImpl extends FilterOutputStream
{
	public WrappedOutputStreamImpl(OutputStream baseOutputStream)
	{
		super(baseOutputStream);
	}

	protected OutputStream getBaseOutputStream()
	{
		return super.out;
	}

	@Override
	public void close() throws IOException
	{
		super.close();
	}

	@Override
	public void flush() throws IOException
	{
		super.flush();
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		super.write(b, off, len);
	}

	@Override
	public void write(int b) throws IOException
	{
		super.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		super.write(b);
	}
}

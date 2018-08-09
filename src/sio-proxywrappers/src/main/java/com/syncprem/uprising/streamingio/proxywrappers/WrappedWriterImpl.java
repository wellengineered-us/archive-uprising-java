/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class WrappedWriterImpl extends FilterWriter
{
	public WrappedWriterImpl(Writer baseWriter)
	{
		super(baseWriter);
	}

	private Writer getBaseWriter()
	{
		return super.out;
	}

	@Override
	public Writer append(CharSequence csq) throws IOException
	{
		return super.append(csq);
	}

	@Override
	public Writer append(CharSequence csq, int start, int end) throws IOException
	{
		return super.append(csq, start, end);
	}

	@Override
	public Writer append(char c) throws IOException
	{
		return super.append(c);
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
	public void write(int c) throws IOException
	{
		super.write(c);
	}

	@Override
	public void write(char[] cbuf) throws IOException
	{
		super.write(cbuf);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException
	{
		super.write(cbuf, off, len);
	}

	@Override
	public void write(String str) throws IOException
	{
		super.write(str);
	}

	@Override
	public void write(String str, int off, int len) throws IOException
	{
		super.write(str, off, len);
	}
}

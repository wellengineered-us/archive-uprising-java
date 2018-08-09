/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import java.io.IOException;
import java.io.InputStream;

public class ProgressWrappedInputStreamImpl extends WrappedInputStreamImpl
{
	public ProgressWrappedInputStreamImpl(InputStream baseInputStream)
	{
		super(baseInputStream);
	}

	private long total = 0L;

	private long getTotal()
	{
		return total;
	}

	private void setTotal(long total)
	{
		this.total = total;
	}

	@Override
	public void close() throws IOException
	{
		writeMessage("CLOSE");

		super.close();
	}

	@Override
	public int read() throws IOException
	{
		int retval;

		retval = super.read();

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(0, 1, retval);

		return retval;
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		int retval;

		retval = super.read(b);

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(0, 1, retval);

		return retval;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		int retval;

		retval = super.read(b, off, len);

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(off, len, retval);

		return retval;
	}

	@Override
	public byte[] readAllBytes() throws IOException
	{
		byte[] retval;

		retval = super.readAllBytes();

		if (retval != null)
		{
			this.setTotal(this.getTotal() + retval.length);
			this.writeProgress(0, -1, retval.length);
		}

		return retval;
	}

	@Override
	public int readNBytes(byte[] b, int off, int len) throws IOException
	{
		int retval;

		retval = super.readNBytes(b, off, len);

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(off, len, retval);

		return retval;
	}

	@Override
	public long skip(long n) throws IOException
	{
		long retval;

		retval = super.skip(n);

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(n, -1, retval);

		return retval;
	}

	private void writeMessage(String message)
	{
		System.out.println(message);
	}

	private void writeProgress(long offset, long count, long retval)
	{
		//System.out.println(String.format("WRITE: offset=%s, count=%s; retval=%s; total=%s", offset, count, retval, this.getTotal()));
	}
}

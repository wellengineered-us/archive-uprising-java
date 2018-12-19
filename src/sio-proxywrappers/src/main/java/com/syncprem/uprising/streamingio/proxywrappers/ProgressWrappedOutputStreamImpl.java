/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers;

import java.io.IOException;
import java.io.OutputStream;

public class ProgressWrappedOutputStreamImpl extends WrappedOutputStreamImpl
{
	public ProgressWrappedOutputStreamImpl(OutputStream baseOutputStream)
	{
		super(baseOutputStream);
	}

	protected static final int DEFAULT_STREAM_WRITE_PUNCTUATE_MODULO = 1000;
	private long total = 0L;

	public long getTotal()
	{
		return this.total;
	}

	private void setTotal(long total)
	{
		this.total = total;
	}

	@Override
	public void flush() throws IOException
	{
		super.flush();
		this.setTotal(0L);
	}

	@Override
	public void write(int b) throws IOException
	{
		final int off = 0;
		final int len = 1;
		int retval;

		super.write(b);
		retval = len;

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(off, len, retval);
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		final int off = 0;
		int len;
		int retval;

		if (b == null)
			return;

		len = b.length;

		super.write(b);
		retval = len;

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(off, len, retval);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		int retval;

		super.write(b, off, len);
		retval = len;

		this.setTotal(this.getTotal() + retval);
		this.writeProgress(off, len, retval);
	}

	private void writeProgress(long offset, long count, long retval)
	{
		/*if (((this.getTotal() + 1) % DEFAULT_STREAM_WRITE_PUNCTUATE_MODULO) == 0)
			System.out.println(String.format("[%s %s WRITE: offset=%s, count=%s; retval=%s; total=%s]",
					formatObjectInfo(this), formatCurrentThreadId(),
					offset, count, retval, this.getTotal()));*/
	}
}

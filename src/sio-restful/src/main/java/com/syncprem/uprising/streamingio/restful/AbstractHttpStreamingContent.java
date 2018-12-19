/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.TryOut;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractHttpStreamingContent extends AbstractLifecycle<Exception, Exception> implements HttpStreamingContent
{
	protected AbstractHttpStreamingContent()
	{
	}

	@Override
	public final Long getLength() throws IOException
	{
		final TryOut<Long> longTryOut = new TryOut<>();

		if (this.tryComputeLength(longTryOut) &&
				longTryOut.isSet() && (longTryOut.getValue() >= 0 || longTryOut.getValue() <= Integer.MAX_VALUE))
			return (long) longTryOut.getValue();
		else
			return null;
	}

	@Override
	public final void copyTo(OutputStream outputStream) throws IOException
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		this.serializeToOutputStream(outputStream);
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		// do nothing
	}

	protected abstract InputStream createContentInputStream() throws IOException;

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		// do nothing
	}

	@Override
	public final InputStream readFrom() throws IOException
	{
		final InputStream inputStream = this.createContentInputStream();
		return inputStream;
	}

	protected abstract void serializeToOutputStream(OutputStream outputStream) throws IOException;

	protected abstract boolean tryComputeLength(TryOut<Long> value) throws IOException;
}

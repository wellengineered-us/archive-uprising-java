/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.TryOut;
import com.syncprem.uprising.streamingio.proxywrappers.strategies.IOWriteWrapperStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class PushOutputStreamContent extends AbstractHttpStreamingContent
{
	public PushOutputStreamContent(IOWriteWrapperStrategy pushOutputStreamCallback)
	{
		if (pushOutputStreamCallback == null)
			throw new ArgumentNullException("pushOutputStreamCallback");

		this.pushOutputStreamCallback = pushOutputStreamCallback;
	}

	private final IOWriteWrapperStrategy pushOutputStreamCallback;

	private IOWriteWrapperStrategy getPushOutputStreamCallback()
	{
		return this.pushOutputStreamCallback;
	}

	@Override
	protected InputStream createContentInputStream() throws IOException
	{
		return null; // not supported as it does not make any sesne
	}

	@Override
	protected void serializeToOutputStream(OutputStream outputStream) throws IOException
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		// NOTE: this is an I/O push model...
		// e.g. do you "push" content to the network, or does the infrastructure "pull" it from you
		try (outputStream)
		{
			this.getPushOutputStreamCallback().wrap(outputStream);
			// ignore return value from callback
		}
	}

	@Override
	protected boolean tryComputeLength(TryOut<Long> value) throws IOException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		return false;
	}
}

/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.TryOut;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class PullOutputStreamContent extends AbstractHttpStreamingContent
{
	public PullOutputStreamContent(InputStream inputContentStream)
	{
		if (inputContentStream == null)
			throw new ArgumentNullException("inputContentStream");

		this.inputContentStream = inputContentStream;
	}

	private final InputStream inputContentStream;

	private InputStream getInputContentStream()
	{
		return this.inputContentStream;
	}

	@Override
	protected InputStream createContentInputStream() throws IOException
	{
		return this.getInputContentStream();
	}

	@Override
	protected void serializeToOutputStream(OutputStream outputStream) throws IOException
	{
		final InputStream inputStream = this.getInputContentStream();
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		try (inputStream)
		{
			try (outputStream)
			{
				Utils.streamCopy(inputStream, outputStream);
			}
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

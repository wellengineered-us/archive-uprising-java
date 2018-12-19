/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.TryOut;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class EmptyStreamContent extends AbstractHttpStreamingContent
{
	public EmptyStreamContent()
	{
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

		try (outputStream)
		{
			// do nothing
		}
	}

	@Override
	protected boolean tryComputeLength(TryOut<Long> value) throws IOException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		value.setValue(0L);
		return true;
	}
}

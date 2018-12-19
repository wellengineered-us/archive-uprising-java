/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers.strategies;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.streamingio.proxywrappers.ProgressWrappedInputStreamImpl;
import com.syncprem.uprising.streamingio.proxywrappers.ProgressWrappedOutputStreamImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class GZipCompressionStrategyImpl implements CompressionStrategy
{
	public GZipCompressionStrategyImpl()
	{
	}

	@Override
	public InputStream wrap(InputStream inputStream) throws IOException
	{
		if (inputStream == null)
			throw new ArgumentNullException("inputStream");

		inputStream = new ProgressWrappedInputStreamImpl(inputStream); // compressed
		inputStream = new GZIPInputStream(inputStream);

		return inputStream;
	}

	@Override
	public OutputStream wrap(OutputStream outputStream) throws IOException
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		outputStream = new ProgressWrappedOutputStreamImpl(outputStream); // compressed
		outputStream = new GZIPOutputStream(outputStream);

		return outputStream;
	}
}
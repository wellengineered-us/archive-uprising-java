/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers.strategies;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class NoneCompressionStrategyImpl implements CompressionStrategy
{
	public NoneCompressionStrategyImpl()
	{
	}

	@Override
	public InputStream wrap(InputStream inputStream) throws IOException
	{
		if (inputStream == null)
			throw new ArgumentNullException("inputStream");

		return inputStream;
	}

	@Override
	public OutputStream wrap(OutputStream outputStream) throws IOException
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		return outputStream;
	}
}
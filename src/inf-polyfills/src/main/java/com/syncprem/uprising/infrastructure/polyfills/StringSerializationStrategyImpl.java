/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StringSerializationStrategyImpl implements SerializationStrategy
{
	public StringSerializationStrategyImpl()
	{
	}

	@Override
	public void setObjectToStream(OutputStream outputStream, Object obj) throws Exception
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream))
		{
			final String value = Utils.toStringSafe(obj);
			System.out.println(value);
			outputStreamWriter.write(value);
			outputStreamWriter.flush();
			//outputStream.flush();
		}
	}
}
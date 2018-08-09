/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

public class JsonSerializationStrategyImpl implements SerializationStrategy
{
	public JsonSerializationStrategyImpl()
	{
	}

	private static <T> void writeJsonObject(OutputStream outputStream, T obj)
	{
		ObjectMapper objectMapper;
		ObjectWriter objectWriter;

		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try
		{
			objectMapper = new ObjectMapper();
			objectWriter = objectMapper
					.configure(AUTO_CLOSE_TARGET, false)
					.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
					.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
					.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, false)
					.writer();

			objectWriter.writeValue(outputStream, obj);
		}
		catch (IOException ioex)
		{
			//ioex.printStackTrace();
		}
	}

	@Override
	public void setObjectToStream(OutputStream outputStream, Object obj) throws Exception
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (obj == null)
			throw new ArgumentNullException("obj");

		if (obj instanceof Iterator<?>)
		{
			final Iterator<?> iterator = (Iterator<?>) obj;
			while (iterator.hasNext())
			{
				final Object item = iterator.next();

				writeJsonObject(outputStream, item);
				//writeJsonObject(System.out, item);
			}
		}
	}
}
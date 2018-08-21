/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentOutOfRangeException;
import com.syncprem.uprising.infrastructure.polyfills.NotImplementedException;

import java.io.*;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

public class JsonSerializationStrategyImpl implements SerializationStrategy, NativeSerializationStrategy<JsonParser, JsonGenerator>
{
	public JsonSerializationStrategyImpl()
	{
	}

	public static <Tin, Tout extends Map<?, ?>> Tout getMapFromJsonObject(Tin value, Class<Tout> clazz)
	{
		return convertObject(value, clazz);
	}

	public static <Tin extends Map<?, ?>, Tout> Tout getObjectFromJsonMap(Tin value, Class<Tout> clazz)
	{
		return convertObject(value, clazz);
	}

	private static <Tin, Tout> Tout convertObject(Tin value, Class<Tout> clazz)
	{
		Tout obj;

		if (value == null)
			throw new ArgumentNullException("value");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.convertValue(value, clazz);

		return obj;
	}

	@Override
	public <TObject> TObject deserializeObjectFromByteStream(Class<? extends TObject> clazz, InputStream inputStream) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		//if (inputStream.available() <= 0)
			//return null;

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(inputStream, clazz);

		return obj;
	}

	@Override
	public <TObject> TObject deserializeObjectFromBytes(Class<? extends TObject> clazz, byte[] inputBytes) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> TObject deserializeObjectFromCharStream(Class<? extends TObject> clazz, Reader inputReader) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> TObject deserializeObjectFromFile(Class<? extends TObject> clazz, String inputFilePath) throws Exception
	{
		TObject obj;

		if (inputFilePath == null)
			throw new ArgumentNullException("inputFilePath");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (inputFilePath.isEmpty())
			throw new ArgumentOutOfRangeException("inputFilePath");

		final ObjectMapper objectMapper = getObjectMapper();

		try (FileReader fileReader = new FileReader(inputFilePath))
		{
			obj = objectMapper.readValue(fileReader, clazz);
		}

		return obj;
	}

	@Override
	public <TObject> TObject deserializeObjectFromNative(Class<? extends TObject> clazz, JsonParser nativeInput) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> TObject deserializeObjectFromString(Class<? extends TObject> clazz, String inputString) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> String serializeObjectToString(Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> void serializeObjectToNative(JsonGenerator nativeOutput, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> void serializeObjectToFile(String outputFilePath, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> void serializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (outputWriter == null)
			throw new ArgumentNullException("outputWriter");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		// TODO: verify this is stream IO oriented
		objectWriter.writeValue(outputWriter, obj);
	}

	@Override
	public <TObject> byte[] serializeObjectToBytes(Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> void serializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		// TODO: verify this is stream IO oriented
		objectWriter.writeValue(outputStream, obj);
	}

	private static ObjectMapper getObjectMapper()
	{
		ObjectMapper objectMapper;

		objectMapper = new ObjectMapper();
		objectMapper
				.configure(AUTO_CLOSE_TARGET, false)
				.configure(ALLOW_COMMENTS, true)
				.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

		return objectMapper;
	}

	private static ObjectWriter getObjectWriter()
	{
		ObjectWriter objectWriter;

		final ObjectMapper objectMapper = getObjectMapper();
		objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		return objectWriter;
	}
}
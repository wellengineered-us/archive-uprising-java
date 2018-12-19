/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

public class JsonSerializationStrategyImpl extends AbstractSerializationStrategy<JsonParser, JsonGenerator>
{
	public JsonSerializationStrategyImpl()
	{
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

	public static <Tin extends Map<?, ?>, Tout> Tout getObjectFromJsonMap(Tin value, Class<Tout> clazz)
	{
		return convertObject(value, clazz);
	}

	@Override
	public <TObject> TObject deserializeObjectFromByteStream(Class<? extends TObject> clazz, InputStream inputStream) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (inputStream == null)
			throw new ArgumentNullException("inputStream");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(inputStream, clazz);

		return obj;
	}

	@Override
	public <TObject> TObject deserializeObjectFromCharStream(Class<? extends TObject> clazz, Reader inputReader) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (inputReader == null)
			throw new ArgumentNullException("inputReader");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(inputReader, clazz);

		return obj;
	}

	@Override
	public <TObject> TObject deserializeObjectFromNative(Class<? extends TObject> clazz, JsonParser nativeInput) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (nativeInput == null)
			throw new ArgumentNullException("nativeInput");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(nativeInput, clazz);

		return obj;
	}

	@Override
	public <TObject> void serializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		objectWriter.writeValue(outputStream, obj);
	}

	@Override
	public <TObject> void serializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (outputWriter == null)
			throw new ArgumentNullException("outputWriter");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		objectWriter.writeValue(outputWriter, obj);
	}

	@Override
	public <TObject> void serializeObjectToNative(JsonGenerator nativeOutput, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (nativeOutput == null)
			throw new ArgumentNullException("nativeOutput");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		objectWriter.writeValue(nativeOutput, obj);
	}
}
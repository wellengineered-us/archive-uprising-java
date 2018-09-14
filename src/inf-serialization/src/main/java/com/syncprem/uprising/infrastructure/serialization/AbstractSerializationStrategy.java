/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentOutOfRangeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public abstract class AbstractSerializationStrategy<TNativeInput, TNativeOutput> implements SerializationStrategy, NativeSerializationStrategy<TNativeInput, TNativeOutput>
{
	public AbstractSerializationStrategy()
	{
	}

	@Override
	public final <TObject> TObject deserializeObjectFromBytes(Class<? extends TObject> clazz, byte[] inputBytes) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (inputBytes == null)
			throw new ArgumentNullException("inputBytes");

		if (inputBytes.length == 0)
			throw new ArgumentOutOfRangeException("inputBytes");

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes))
		{
			obj = this.deserializeObjectFromByteStream(clazz, byteArrayInputStream);
		}

		return obj;
	}

	@Override
	public final <TObject> TObject deserializeObjectFromString(Class<? extends TObject> clazz, String inputString) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (inputString == null)
			throw new ArgumentNullException("inputString");

		if (inputString.isEmpty())
			throw new ArgumentOutOfRangeException("inputString");

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes()))
		{
			obj = this.deserializeObjectFromByteStream(clazz, byteArrayInputStream);
		}

		return obj;
	}

	@Override
	public final <TObject> String serializeObjectToString(Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
		{
			this.serializeObjectToByteStream(byteArrayOutputStream, clazz, obj);

			final byte[] bresult = byteArrayOutputStream.toByteArray();
			final String result = new String(bresult);

			return result;
		}
	}

	@Override
	public final <TObject> byte[] serializeObjectToBytes(Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
		{
			this.serializeObjectToByteStream(byteArrayOutputStream, clazz, obj);

			final byte[] result = byteArrayOutputStream.toByteArray();

			return result;
		}
	}

	@Override
	public final <TObject> TObject deserializeObjectFromFile(Class<? extends TObject> clazz, String inputFilePath) throws Exception
	{
		TObject obj;

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (inputFilePath == null)
			throw new ArgumentNullException("inputFilePath");

		if (inputFilePath.isEmpty())
			throw new ArgumentOutOfRangeException("inputFilePath");

		try (FileInputStream fileInputStream = new FileInputStream(inputFilePath))
		{
			obj = this.deserializeObjectFromByteStream(clazz, fileInputStream);
		}

		return obj;
	}

	@Override
	public final <TObject> void serializeObjectToFile(String outputFilePath, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		if (outputFilePath == null)
			throw new ArgumentNullException("outputFilePath");

		if (outputFilePath.isEmpty())
			throw new ArgumentOutOfRangeException("outputFilePath");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath))
		{
			this.serializeObjectToByteStream(fileOutputStream, clazz, obj);
		}
	}
}
/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.serialization;

import com.syncprem.uprising.infrastructure.polyfills.NotImplementedException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class StringSerializationStrategyImpl implements SerializationStrategy, NativeSerializationStrategy<CharSequence, CharSequence>
{
	public StringSerializationStrategyImpl()
	{
	}


	@Override
	public <TObject> TObject deserializeObjectFromByteStream(Class<? extends TObject> clazz, InputStream inputStream) throws Exception
	{
		throw new NotImplementedException();
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
		throw new NotImplementedException();
	}

	@Override
	public <TObject> TObject deserializeObjectFromNative(Class<? extends TObject> clazz, CharSequence charSequence) throws Exception
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
	public <TObject> void serializeObjectToNative(CharSequence charSequence, Class<? extends TObject> clazz, TObject obj) throws Exception
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
		throw new NotImplementedException();
	}

	@Override
	public <TObject> byte[] serializeObjectToBytes(Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	public <TObject> void serializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> clazz, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}
}
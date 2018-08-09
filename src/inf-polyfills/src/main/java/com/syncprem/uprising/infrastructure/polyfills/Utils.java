/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

public final class Utils
{
	public static final String EMPTY_STRING = "";

	private static <Tin, Tout> Tout convertObject(Tin value, Class<Tout> clazz)
	{
		ObjectMapper objectMapper;
		Tout obj;

		if (value == null)
			throw new ArgumentNullException("value");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		objectMapper = new ObjectMapper();
		objectMapper
				.configure(ALLOW_COMMENTS, true)
				.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

		obj = objectMapper.convertValue(value, clazz);
		return obj;
	}

	public static <T> T failFastOnlyWhen(boolean conditional, String label) throws FailFastException
	{
		if (conditional)
		{
			final String message = String.format("Fail-fast assertion failed: '%s'", label);
			System.err.println(message);
			throw new FailFastException(message); // no catching this
		}

		return nop();
	}

	public static <T> T failFastWithException(Exception ex)
	{
		ex.printStackTrace();
		System.exit(-2);
		return nop();
	}

	public static String formatCallerInfo()
	{
		return formatCallerInfo(1 + 1);
	}

	public static String formatCallerInfo(int skipFrames)
	{
		final int SKIP_FRAMES = skipFrames + 1;
		final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		final StackWalker.StackFrame stackFrame = stackWalker.walk(frames -> frames.skip(SKIP_FRAMES).findFirst()).get();

		return String.format("%s::%s(),%s", stackFrame.getClassName(), stackFrame.getMethodName(), formatCurrentThreadId());
	}

	public static String formatCurrentThreadId()
	{
		return String.format("%05X", Thread.currentThread().getId());
	}

	public static String formatObjectInfo(Object obj)
	{
		if (obj == null)
			throw new ArgumentNullException("obj");

		return String.format("%s@%05X", obj.getClass().getName(), System.identityHashCode(obj));
	}

	public static String formatUUID(UUID uuid)
	{
		if (uuid == null)
			throw new ArgumentNullException("uuid");

		return uuid.toString().replace("-", "").toUpperCase();
	}

	public static <Tin, Tout extends Map<?, ?>> Tout getMapFromJsonObject(Tin value, Class<Tout> clazz)
	{
		return convertObject(value, clazz);
	}


	/**
	 * TODO: move to JsonSerializationStrategyImpl.java
	 *
	 * @param filePath
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getObjectFromJsonFile(String filePath, Class<T> clazz)
	{
		ObjectMapper objectMapper;
		T obj;

		if (filePath == null)
			throw new ArgumentNullException("filePath");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		if (filePath.isEmpty())
			throw new ArgumentOutOfRangeException("filePath");

		try
		{
			objectMapper = new ObjectMapper();
			objectMapper
					.configure(ALLOW_COMMENTS, true)
					.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
					.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
					.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
			try (FileReader fileReader = new FileReader(filePath))
			{
				obj = objectMapper.readValue(fileReader, clazz);
				return obj;
			}
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			return null;
		}
	}

	public static <Tin extends Map<?, ?>, Tout> Tout getObjectFromJsonMap(Tin value, Class<Tout> clazz)
	{
		return convertObject(value, clazz);
	}

	public static <T> T getValueOrDefault(T value, final T defaultValue)
	{
		value = value == null ? defaultValue : value;
		return value;
	}

	public static boolean hitWithBrick(Object obj) throws Exception
	{
		boolean result = false;
		if (obj != null)
		{
			if (obj instanceof Closeable)
			{
				((Closeable) obj).close();
				result = true;
			}
			else if (obj instanceof AutoCloseable)
			{
				((AutoCloseable) obj).close();
				result = true;
			}
		}

		return result;
	}

	public static boolean isNullOrEmptyString(String value)
	{
		return value == null || value.isEmpty();
	}

	public static <T> Class<? extends T> loadClassByName(String className, Class<T> subClazz)
	{
		if (className == null)
			throw new ArgumentNullException("className");

		if (subClazz == null)
			throw new ArgumentNullException("subClazz");

		if (className.isEmpty())
			throw new ArgumentOutOfRangeException("className");

		try
		{
			return Class.forName(className).asSubclass(subClazz);
		}
		catch (ClassNotFoundException ex)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> loadClassByName(String className)
	{
		if (className == null)
			throw new ArgumentNullException("className");

		if (className.isEmpty())
			throw new ArgumentOutOfRangeException("className");

		try
		{
			return (Class<? extends T>) Class.forName(className);
		}
		catch (ClassNotFoundException ex)
		{
			return null;
		}
	}

	public static boolean maybeCreate(Object obj) throws Exception
	{
		boolean result = false;
		if (obj != null)
		{
			if (obj instanceof Creatable)
			{
				((Creatable) obj).create();
				result = true;
			}
		}

		return result;
	}

	public static boolean maybeDispose(Object obj) throws Exception
	{
		boolean result = false;
		if (obj != null)
		{
			if (obj instanceof Disposable)
			{
				((Disposable) obj).dispose();
				result = true;
			}
		}

		return result;
	}

	public static <T> T newObjectFromClass(Class<? extends T> clazz)
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		try
		{
			return clazz.getConstructor().newInstance();
		}
		catch (NoSuchMethodException ex)
		{
			return null;
		}
		catch (IllegalAccessException ex)
		{
			return null;
		}
		catch (InvocationTargetException ex)
		{
			return null;
		}
		catch (InstantiationException ex)
		{
			return null;
		}
	}

	public static <T> T nop()
	{
		return null;
	}

	public static long streamCopy(InputStream source, OutputStream destination) throws IOException
	{
		if (source == null)
			throw new ArgumentNullException("source");

		if (destination == null)
			throw new ArgumentNullException("destination");

		final int BUFFER_SIZE = 4096;
		final byte[] buffer = new byte[BUFFER_SIZE];
		long total = 0L;

		while (true)
		{
			final int b = source.read(buffer);

			if (b == -1)
				break;

			destination.write(buffer, 0, b);
			total += b;
		}

		return total;
	}

	public static String toStringSafe(Object value)
	{
		return value == null ? EMPTY_STRING : value.toString();
	}

	/*public static <K, V> boolean tryGetValue(Map<K, V> thisMap, K key, TryOut<V> outValue)
	{
		V value;

		if (thisMap == null)
			throw new ArgumentNullException("thisMap");

		if (outValue == null)
			throw new ArgumentNullException("outValue");

		if(!thisMap.containsKey(key))
			return false;

		value = thisMap.get(key);
		outValue.setValue(value);
		return true;
	}*/

	/*public static <K, V> boolean tryPutValue(Map<K, V> thisMap, K key, V value)
	{
		if (thisMap == null)
			throw new ArgumentNullException("thisMap");

		if(thisMap.containsKey(key))
			return false;

		thisMap.put(key, value);
		return true;
	}*/
}

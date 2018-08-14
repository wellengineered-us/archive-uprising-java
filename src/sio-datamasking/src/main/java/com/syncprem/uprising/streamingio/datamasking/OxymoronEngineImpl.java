/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

import com.syncprem.uprising.infrastructure.polyfills.*;
import com.syncprem.uprising.streamingio.primitives.Field;
import com.syncprem.uprising.streamingio.primitives.FieldImpl;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.primitives.PayloadImpl;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

public class OxymoronEngineImpl extends AbstractLifecycle<Exception, Exception> implements Creatable, Disposable, OxymoronEngine, ObfuscationContext
{
	public OxymoronEngineImpl(ResolveDictionaryValueDelegate resolveDictionaryValueCallback, ObfuscationSpec obfuscationSpec)
	{
		if (resolveDictionaryValueCallback == null)
			throw new ArgumentNullException("resolveDictionaryValueCallback");

		if (obfuscationSpec == null)
			throw new ArgumentNullException("obfuscationSpec");

		this.resolveDictionaryValueCallback = resolveDictionaryValueCallback;
		this.obfuscationSpec = obfuscationSpec;

		obfuscationSpec.assertValid();
	}

	private final long DEFAULT_HASH_BUCKET_SIZE = Long.MAX_VALUE;
	private final boolean SUBSTITUTION_CACHE_ENABLED = true;
	private final Map<Long, ColumnSpec<?>> fieldCache = new LinkedHashMap<>();
	private final ObfuscationSpec obfuscationSpec;
	private final Map<String, ObfuscationStrategy> obfuscationStrategyCache = new LinkedHashMap<>();
	private final ResolveDictionaryValueDelegate resolveDictionaryValueCallback;
	private final Map<String, Map<Object, Object>> substitutionCacheRoot = new LinkedHashMap<>();

	private Map<Long, ColumnSpec<?>> getFieldCache()
	{
		return this.fieldCache;
	}

	private ObfuscationSpec getObfuscationSpec()
	{
		return this.obfuscationSpec;
	}

	private Map<String, ObfuscationStrategy> getObfuscationStrategyCache()
	{
		return this.obfuscationStrategyCache;
	}

	private ResolveDictionaryValueDelegate getResolveDictionaryValueCallback()
	{
		return this.resolveDictionaryValueCallback;
	}

	private Map<String, Map<Object, Object>> getSubstitutionCacheRoot()
	{
		return this.substitutionCacheRoot;
	}

	private static <T> T exit_yield()
	{
		return null;
	}

	private static Long getHash(Long multiplier, Long size, Long seed, Object value)
	{
		final long DEFAULT_HASH = -1L;
		long hashCode;
		byte[] buffer;
		Class<?> valueClass;
		String _value;

		if (multiplier == null)
			return null;

		if (size == null)
			return null;

		if (seed == null)
			return null;

		if (size == 0L)
			return null; // prevent DIV0

		if (value == null)
			return null;

		valueClass = value.getClass();

		if (!valueClass.equals(String.class))
			return null;

		_value = (String) value;

		if (Utils.isNullOrEmptyString(_value))
			return DEFAULT_HASH;

		_value = _value.trim();

		buffer = StandardCharsets.UTF_8.encode(_value).array();

		hashCode = (long) seed;
		for (int index = 0; index < buffer.length; index++)
			hashCode = ((long) multiplier * hashCode + buffer[index]) % 0xFFFFFFFF;

		if (hashCode > 0x7FFFFFFF)
			hashCode = hashCode - 0xFFFFFFFF;

		if (hashCode < 0L)
			hashCode = hashCode + 0x7FFFFFFF;

		hashCode = (hashCode % (long) size);

		return (long) (int) hashCode;
	}

	private static <T> T yield(Object value)
	{
		return null;
	}

	private Object _getObfuscatedValue(Field field, Object originalFieldValue)
	{
		ColumnSpec<?> columnSpec;
		ObfuscationStrategy obfuscationStrategy;
		Object obfuscatedFieldValue;

		if (field == null)
			throw new ArgumentNullException("field");

		if (!this.getFieldCache().containsKey(field.getFieldIndex()))
		{
			//StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.getObfuscationSpec().getTableSpec().getColumnSpecs(), Spliterator.ORDERED)
			columnSpec = StreamSupport.stream(this.getObfuscationSpec().getTableSpec().getColumnSpecs().spliterator(), false)
					.filter(c -> Utils.toStringSafe(c.getColumnName()).trim().toLowerCase() == Utils.toStringSafe(field.getFieldName()).trim().toLowerCase())
					.findFirst()
					.get();

			this.getFieldCache().put(field.getFieldIndex(), columnSpec);
		}
		else
			columnSpec = this.getFieldCache().get(field.getFieldIndex());

		if (columnSpec == null)
			return originalFieldValue; // do nothing when no matching column spec

		if (columnSpec.getObfuscationStrategyClass() == null)
			return originalFieldValue; // do nothing when strategy type not set

		if (!this.getObfuscationStrategyCache().containsKey(columnSpec.getObfuscationStrategyClass().getName()))
		{
			obfuscationStrategy = Utils.newObjectFromClass(columnSpec.getObfuscationStrategyClass());

			if (obfuscationStrategy == null)
				throw new InvalidOperationException(String.format("Unknown obfuscation strategy '%s' specified for column '%s'.", columnSpec.getObfuscationStrategyClass().getName(), field.getFieldName()));

			this.getObfuscationStrategyCache().put(columnSpec.getObfuscationStrategyClass().getName(), obfuscationStrategy);
		}
		else
			obfuscationStrategy = this.getObfuscationStrategyCache().get(columnSpec.getObfuscationStrategyClass().getName());

		obfuscatedFieldValue = obfuscationStrategy.getObfuscatedValue(this, columnSpec, field, originalFieldValue);

		return obfuscatedFieldValue;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			// do nothing
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			this.getSubstitutionCacheRoot().clear();
			this.getFieldCache().clear();
		}
	}

	private long getBoundedHash(Long size, Object value)
	{
		Long hash;

		hash = getHash(this.getObfuscationSpec().getHashSpec().getMultiplier(),
				size,
				this.getObfuscationSpec().getHashSpec().getSeed(),
				Utils.toStringSafe(value));

		if (hash == null)
			throw new InvalidOperationException(String.format("Oxymoron engine failed to calculate a valid hash for input '%s'.", value));

		return hash;
	}

	@Override
	public Object getObfuscatedValue(Field field, Object originalFieldValue)
	{
		Object value;

		if (field == null)
			throw new ArgumentNullException("field");

		if (this.getObfuscationSpec().enablePassThru())
			return originalFieldValue; // pass-thru (safety switch)

		value = this._getObfuscatedValue(field, originalFieldValue);

		return value;
	}

	@Override
	public Iterator<Payload> getObfuscatedValues(Iterator<Payload> records)
	{
		String fieldName;
		Class<?> fieldClass;
		Object originalFieldValue, obfuscatedFieldValue;

		if (records == null)
			throw new ArgumentNullException("records");

		while (records.hasNext())
		{
			final Payload record = records.next();
			Payload obfuscatedPayload = null;

			if (record != null)
			{
				obfuscatedPayload = new PayloadImpl(record.size());

				for (Map.Entry<String, Object> item : record.entrySet())
				{
					FieldImpl field; // TODO: should be provided to constructor

					if (item == null)
						continue;

					fieldName = item.getKey();
					originalFieldValue = item.getValue();
					fieldClass = originalFieldValue == null ? Object.class : originalFieldValue.getClass();

					field = new FieldImpl();
					field.setFieldName(fieldName);

					obfuscatedFieldValue = this.getObfuscatedValue(field, originalFieldValue);
					obfuscatedPayload.put(fieldName, obfuscatedFieldValue);
				}
			}

			return yield(obfuscatedPayload); // TODO add wrapped something or another here
		}

		return exit_yield();
	}

	@Override
	public long getSignHash(Object value)
	{
		long hash;

		hash = this.getBoundedHash(DEFAULT_HASH_BUCKET_SIZE, value);

		return hash;
	}

	@Override
	public long getValueHash(Long size, Object value)
	{
		long hash;

		hash = this.getBoundedHash(size == null ? DEFAULT_HASH_BUCKET_SIZE : size, value);

		return hash;
	}

	private Object resolveDictionaryValue(DictionarySpec dictionarySpec, Object surrogateKey)
	{
		if (dictionarySpec == null)
			throw new ArgumentNullException("dictionarySpec");

		if (surrogateKey == null)
			throw new ArgumentNullException("surrogateKey");

		if (this.getResolveDictionaryValueCallback() == null)
			return null;

		try
		{
			return this.getResolveDictionaryValueCallback().invoke(dictionarySpec, surrogateKey);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	@Override
	public boolean tryGetSurrogateValue(final DictionarySpec dictionarySpec, Object surrogateKey, final TryOut<Object> tryOutSurrogateValue)
	{
		final TryOut<Map<Object, Object>> tryOutDictionaryCache = new TryOut<>();
		Map<Object, Object> dictionaryCache;
		Object surrogateValue;

		if (dictionarySpec == null)
			throw new ArgumentNullException("dictionarySpec");

		if (tryOutSurrogateValue == null)
			throw new ArgumentNullException("tryOutSurrogateValue");

		if (!this.getObfuscationSpec().disableEngineCaches())
		{
			if (!this.getSubstitutionCacheRoot().containsKey(dictionarySpec.getDictionaryId()))
				this.getSubstitutionCacheRoot().put(dictionarySpec.getDictionaryId(), (dictionaryCache = new LinkedHashMap<>()));
			else
				dictionaryCache = this.getSubstitutionCacheRoot().get(dictionarySpec.getDictionaryId());

			if (!dictionaryCache.containsKey(surrogateKey))
			{
				if (dictionarySpec.preloadEnabled())
					throw new InvalidOperationException(String.format("Cache miss when dictionary preload enabled for dictionary ID '%s'; current cache slot item count: %s.", dictionarySpec.getDictionaryId(), dictionaryCache.keySet().size()));

				dictionaryCache.put(surrogateKey, (surrogateValue = this.resolveDictionaryValue(dictionarySpec, surrogateKey)));
			}
			else
				surrogateValue = dictionaryCache.get(surrogateKey);
		}
		else
		{
			surrogateValue = this.resolveDictionaryValue(dictionarySpec, surrogateKey);
			tryOutSurrogateValue.setValue(surrogateValue);
		}

		return true;
	}
}

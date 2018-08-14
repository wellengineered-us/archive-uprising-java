/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class KeyValueViewImpl implements KeyValueView
{
	public KeyValueViewImpl(Schema originalSchema, Payload originalPayload)
	{
		Payload key, value;
		SchemaBuilder k, v;

		if (originalSchema == null)
			throw new ArgumentNullException("originalSchema");

		if (originalPayload == null)
			throw new ArgumentNullException("originalPayload");

		final Map<Boolean, List<Field>> orderedGroups = originalSchema.getFields().values().stream().sorted(Comparator.comparingLong(Field::getFieldIndex)).collect(groupingBy(Field::isFieldKeyComponent));
		final int fieldCount = originalSchema.getFields().size();

		key = new PayloadImpl(fieldCount);
		value = new PayloadImpl(fieldCount);
		k = new SchemaBuilderImpl();
		v = new SchemaBuilderImpl();

		for (Map.Entry<Boolean, List<Field>> orderedGroup : orderedGroups.entrySet())
		{
			for (Field field : orderedGroup.getValue())
			{
				String fieldName;
				Object fieldValue;

				if (field == null)
					continue;

				fieldName = field.getFieldName();
				if (!originalPayload.containsKey(fieldName))
					originalPayload.put(fieldName, (fieldValue = null));
				else
					fieldValue = originalPayload.get(fieldName);

				(orderedGroup.getKey() ? key : value).put(fieldName, fieldValue);
				(orderedGroup.getKey() ? k : v).addField(field.getFieldName(), field.getFieldClass(), field.isFieldOptional(), field.isFieldKeyComponent(), field.getFieldSchema());
			}
		}

		this.originalSchema = originalSchema;
		this.originalPayload = originalPayload;
		this.keyPayload = key;
		this.valuePayload = value;
		this.keySchema = k.build();
		this.valueSchema = v.build();
	}

	public KeyValueViewImpl(Schema originalSchema, Payload originalPayload,
							Schema keySchema, Payload keyPayload,
							Schema valueSchema, Payload valuePayload)
	{
		if (originalSchema == null)
			throw new ArgumentNullException("originalSchema");

		if (originalPayload == null)
			throw new ArgumentNullException("originalPayload");

		if (keySchema == null)
			throw new ArgumentNullException("keySchema");

		if (keyPayload == null)
			throw new ArgumentNullException("keyPayload");

		if (valueSchema == null)
			throw new ArgumentNullException("valueSchema");

		if (valuePayload == null)
			throw new ArgumentNullException("valuePayload");

		this.originalSchema = originalSchema;
		this.originalPayload = originalPayload;
		this.keySchema = keySchema;
		this.keyPayload = keyPayload;
		this.valueSchema = valueSchema;
		this.valuePayload = valuePayload;
	}

	private final Payload keyPayload;
	private final Schema keySchema;
	private final Payload originalPayload;
	private final Schema originalSchema;
	private final Payload valuePayload;
	private final Schema valueSchema;

	@Override
	public Payload getKeyPayload()
	{
		return this.keyPayload;
	}

	@Override
	public Schema getKeySchema()
	{
		return this.keySchema;
	}

	@Override
	public Payload getOriginalPayload()
	{
		return this.originalPayload;
	}

	@Override
	public Schema getOriginalSchema()
	{
		return this.originalSchema;
	}

	@Override
	public Payload getValuePayload()
	{
		return this.valuePayload;
	}

	@Override
	public Schema getValueSchema()
	{
		return this.valueSchema;
	}
}

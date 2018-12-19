/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.InvalidOperationException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SchemaBuilderImpl implements SchemaBuilder, Schema
{
	public SchemaBuilderImpl()
	{
		this(new LinkedHashMap<>());
	}

	public SchemaBuilderImpl(Map<String, Field> fields)
	{
		if (fields == null)
			throw new ArgumentNullException("fields");

		this.fields = fields;
	}

	public static final Schema EMPTY = create().withVersion(0).build();
	private final Map<String, Field> fields;
	private String schemaName;
	private SchemaType schemaType;
	private int schemaVersion;

	public Map<String, Field> getFields()
	{
		return Collections.unmodifiableMap(this.fields);
	}

	private Map<String, Field> getMutableFields()
	{
		return this.fields;
	}

	@Override
	public String getSchemaName()
	{
		return this.schemaName;
	}

	private void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	@Override
	public SchemaType getSchemaType()
	{
		return this.schemaType;
	}

	private void setSchemaType(SchemaType schemaType)
	{
		this.schemaType = schemaType;
	}

	@Override
	public int getSchemaVersion()
	{
		return this.schemaVersion;
	}

	private void setSchemaVersion(int schemaVersion)
	{
		this.schemaVersion = schemaVersion;
	}

	private static void assertCanSet(String propertyName, Object propertyValue, Object newValue)
	{
		if (propertyValue != null && propertyValue != newValue)
			throw new InvalidOperationException(String.format("SchemaBuilder: Property '%s' has already been set.", propertyName));
	}

	private static void assertCanSet(String propertyName, boolean propertyValue, boolean newValue)
	{
		if (propertyValue != false && propertyValue != newValue)
			throw new InvalidOperationException(String.format("SchemaBuilder: Property '%s' has already been set.", propertyName));
	}

	private static void assertCanSet(String propertyName, int propertyValue, int newValue)
	{
		if (propertyValue != 0 && propertyValue != newValue)
			throw new InvalidOperationException(String.format("SchemaBuilder: Property '%s' has already been set.", propertyName));
	}

	public static SchemaBuilder create()
	{
		return new SchemaBuilderImpl();
	}

	@Override
	public SchemaBuilder addField(String fieldName, Class<?> fieldClass, boolean isFieldOptional, boolean isFieldKeyComponent, Schema fieldSchema)
	{
		FieldImpl field;

		if (fieldName == null)
			throw new ArgumentNullException("fieldName");

		if (fieldClass == null)
			throw new ArgumentNullException("fieldClass");

		field = new FieldImpl();
		field.setFieldIndex(this.getFields().size());
		field.setFieldName(fieldName);
		field.setFieldClass(fieldClass);
		field.setFieldOptional(isFieldOptional);
		field.setFieldKeyComponent(isFieldKeyComponent);
		field.setFieldSchema(fieldSchema);

		this.getMutableFields().put(fieldName, field);

		return this;
	}

	@Override
	public SchemaBuilder addFields(Iterable<Field> fields)
	{
		if (fields == null)
			throw new ArgumentNullException("fields");

		for (Field field : fields)
		{
			if (field == null)
				continue;

			this.getMutableFields().put(field.getFieldName(), field);
		}

		return this;
	}

	@Override
	public Schema build()
	{
		return this;
	}

	@Override
	public SchemaBuilder withName(String schemaName)
	{
		assertCanSet("schemaName", this.getSchemaName(), schemaName);
		this.setSchemaName(schemaName);
		return this;
	}

	@Override
	public SchemaBuilder withType(SchemaType schemaType)
	{
		assertCanSet("schemaType", this.getSchemaType(), schemaType);
		this.setSchemaType(schemaType);
		return this;
	}

	@Override
	public SchemaBuilder withVersion(int schemaVersion)
	{
		assertCanSet("schemaVersion", this.getSchemaVersion(), schemaVersion);
		this.setSchemaVersion(schemaVersion);
		return this;
	}
}

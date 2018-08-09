/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

public class FieldImpl implements Field
{
	public FieldImpl()
	{
	}

	private Class<?> fieldClass;
	private long fieldIndex;
	private String fieldName;
	private Schema fieldSchema;
	private boolean isFieldKeyComponent;
	private boolean isFieldOptional;

	@Override
	public Class<?> getFieldClass()
	{
		return this.fieldClass;
	}

	public void setFieldClass(Class<?> fieldClass)
	{
		this.fieldClass = fieldClass;
	}

	@Override
	public long getFieldIndex()
	{
		return this.fieldIndex;
	}

	public void setFieldIndex(long fieldIndex)
	{
		this.fieldIndex = fieldIndex;
	}

	@Override
	public String getFieldName()
	{
		return this.fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	@Override
	public Schema getFieldSchema()
	{
		return this.fieldSchema;
	}

	public void setFieldSchema(Schema fieldSchema)
	{
		this.fieldSchema = fieldSchema;
	}

	@Override
	public boolean isFieldKeyComponent()
	{
		return this.isFieldKeyComponent;
	}

	public void setFieldKeyComponent(boolean isFieldKeyComponent)
	{
		this.isFieldKeyComponent = isFieldKeyComponent;
	}

	@Override
	public boolean isFieldOptional()
	{
		return this.isFieldOptional;
	}

	public void setFieldOptional(boolean isFieldOptional)
	{
		this.isFieldOptional = isFieldOptional;
	}
}

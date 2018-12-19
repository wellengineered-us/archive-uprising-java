/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

public abstract class AbstractTextualFieldSpec implements TextualFieldSpec
{
	protected AbstractTextualFieldSpec()
	{
	}

	private String fieldFormat;
	private long fieldOrdinal;
	private String fieldTitle;
	private TextualFieldType fieldType;
	private boolean isFieldIdentity;
	private boolean isFieldRequired;

	@Override
	public String getFieldFormat()
	{
		return this.fieldFormat;
	}

	public void setFieldFormat(String fieldFormat)
	{
		this.fieldFormat = fieldFormat;
	}

	@Override
	public long getFieldOrdinal()
	{
		return this.fieldOrdinal;
	}

	public void setFieldOrdinal(long fieldOrdinal)
	{
		this.fieldOrdinal = fieldOrdinal;
	}

	@Override
	public String getFieldTitle()
	{
		return this.fieldTitle;
	}

	@Override
	public void setFieldTitle(String fieldTitle)
	{
		this.fieldTitle = fieldTitle;
	}

	@Override
	public TextualFieldType getFieldType()
	{
		return this.fieldType;
	}

	public void setFieldType(TextualFieldType fieldType)
	{
		this.fieldType = fieldType;
	}

	@Override
	public boolean isFieldIdentity()
	{
		return this.isFieldIdentity;
	}

	public void setFieldIdentity(boolean isFieldIdentity)
	{
		this.isFieldIdentity = isFieldIdentity;
	}

	@Override
	public boolean isFieldRequired()
	{
		return this.isFieldRequired;
	}

	public void setFieldRequired(boolean isFieldRequired)
	{
		this.isFieldRequired = isFieldRequired;
	}
}

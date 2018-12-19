/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.configuration.AbstractConfigurationObject;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.streamingio.textual.TextualFieldType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTextualFieldConfiguration extends AbstractConfigurationObject
{
	protected AbstractTextualFieldConfiguration()
	{
	}

	private String fieldFormat;
	private Long fieldOrdinal;
	private String fieldTitle;
	private TextualFieldType fieldType;
	private Boolean isFieldIdentity;
	private Boolean isFieldRequired;

	public Class<?> getFieldClass()
	{
		switch (Utils.getValueOrDefault(this.getFieldType(), TextualFieldType.UNKNOWN))
		{
			case TEXT:
				return String.class;
			case CURRENCY:
				return BigDecimal.class;
			case DATETIME:
				return Instant.class;
			case LOGICAL:
				return Boolean.class;
			case NUMBER:
				return Double.class;
			default:
				return Object.class;
		}
	}

	public String getFieldFormat()
	{
		return this.fieldFormat;
	}

	public void setFieldFormat(String fieldFormat)
	{
		this.fieldFormat = fieldFormat;
	}

	public Long getFieldOrdinal()
	{
		return this.fieldOrdinal;
	}

	public void setFieldOrdinal(Long fieldOrdinal)
	{
		this.fieldOrdinal = fieldOrdinal;
	}

	public String getFieldTitle()
	{
		return this.fieldTitle;
	}

	public void setFieldTitle(String fieldTitle)
	{
		this.fieldTitle = fieldTitle;
	}

	public TextualFieldType getFieldType()
	{
		return this.fieldType;
	}

	public void setFieldType(TextualFieldType fieldType)
	{
		this.fieldType = fieldType;
	}

	public void setFieldIdentity(Boolean isFieldIdentity)
	{
		this.isFieldIdentity = isFieldIdentity;
	}

	public void setFieldRequired(Boolean isFieldRequired)
	{
		this.isFieldRequired = isFieldRequired;
	}

	public Boolean isFieldIdentity()
	{
		return this.isFieldIdentity;
	}

	public Boolean isFieldRequired()
	{
		return this.isFieldRequired;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getFieldTitle()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s textual field title is required.", context), Severity.ERROR));

		return messages;
	}
}

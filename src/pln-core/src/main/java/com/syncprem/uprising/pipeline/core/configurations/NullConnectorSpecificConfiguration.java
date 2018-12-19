/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;

import java.util.ArrayList;
import java.util.List;

public class NullConnectorSpecificConfiguration extends ComponentSpecificConfiguration
{
	public NullConnectorSpecificConfiguration()
	{
	}

	private String fieldNameFormat;
	private Long maxFieldCount;
	private Long maxRecordCount;
	private Boolean useRandom;

	public String getFieldNameFormat()
	{
		return this.fieldNameFormat;
	}

	public void setFieldNameFormat(String fieldNameFormat)
	{
		this.fieldNameFormat = fieldNameFormat;
	}

	public Long getMaxFieldCount()
	{
		return this.maxFieldCount;
	}

	public void setMaxFieldCount(Long maxFieldCount)
	{
		this.maxFieldCount = maxFieldCount;
	}

	public Long getMaxRecordCount()
	{
		return this.maxRecordCount;
	}

	public void setMaxRecordCount(Long maxRecordCount)
	{
		this.maxRecordCount = maxRecordCount;
	}

	public void setRandom(Boolean useRandom)
	{
		this.useRandom = useRandom;
	}

	@JsonProperty(value = "UseRandom")
	public Boolean useRandom()
	{
		return this.useRandom;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getFieldNameFormat()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector field name format is required.", context), Severity.ERROR));

		if (this.getMaxRecordCount() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum record count is required.", context), Severity.ERROR));
		else if (this.getMaxRecordCount() <= 0L)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum record count must be greater than zero.", context), Severity.ERROR));

		if (this.getMaxFieldCount() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum field count is required.", context), Severity.ERROR));
		else if (this.getMaxFieldCount() <= 0L)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum field count must be greater than zero.", context), Severity.ERROR));

		return messages;
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;

import java.util.ArrayList;
import java.util.List;

public class NullConnectorSpecificConfiguration extends StageSpecificConfiguration
{
	public NullConnectorSpecificConfiguration()
	{
	}

	private Long maxRandomRecordCount;
	private Long maxRandomFieldCount;
	private String fieldNameFormat;

	public String getFieldNameFormat()
	{
		return this.fieldNameFormat;
	}

	public void setFieldNameFormat(String fieldNameFormat)
	{
		this.fieldNameFormat = fieldNameFormat;
	}

	public Long getMaxRandomRecordCount()
	{
		return this.maxRandomRecordCount;
	}

	public void setMaxRandomRecordCount(Long maxRandomRecordCount)
	{
		this.maxRandomRecordCount = maxRandomRecordCount;
	}

	public Long getMaxRandomFieldCount()
	{
		return this.maxRandomFieldCount;
	}

	public void setMaxRandomFieldCount(Long maxRandomFieldCount)
	{
		this.maxRandomFieldCount = maxRandomFieldCount;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getFieldNameFormat()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector field name format is required.", context), Severity.ERROR));

		if (this.getMaxRandomRecordCount() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum record count is required.", context), Severity.ERROR));
		else if (this.getMaxRandomRecordCount() <= 0L)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum record count must be greater than zero.", context), Severity.ERROR));

		if (this.getMaxRandomFieldCount() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum field count is required.", context), Severity.ERROR));
		else if (this.getMaxRandomFieldCount() <= 0L)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector random maximum field count must be greater than zero.", context), Severity.ERROR));

		return messages;
	}
}

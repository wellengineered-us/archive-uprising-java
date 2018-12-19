/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.util.ArrayList;
import java.util.List;

public class FieldConfiguration extends ComponentConfiguration
{
	public FieldConfiguration()
	{
	}

	private String fieldName;

	public String getFieldName()
	{
		return this.fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	@Override
	public boolean equals(Object obj)
	{
		FieldConfiguration other = null;

		if (obj instanceof FieldConfiguration)
			other = (FieldConfiguration) obj;

		if (other != null)
			return other.getFieldName().compareToIgnoreCase(this.getFieldName()) == 0;

		return false;
	}

	@Override
	public int hashCode()
	{
		if (this.getFieldName() == null)
			return 0;
		else
			return this.getFieldName().hashCode();
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getFieldName()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s field name is required.", context), Severity.ERROR));

		return messages;
	}
}

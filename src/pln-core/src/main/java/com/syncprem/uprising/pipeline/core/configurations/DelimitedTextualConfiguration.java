/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.streamingio.textual.delimited.DelimitedTextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.delimited.DelimitedTextualFieldSpecImpl;
import com.syncprem.uprising.streamingio.textual.delimited.DelimitedTextualSpec;
import com.syncprem.uprising.streamingio.textual.delimited.DelimitedTextualSpecImpl;

import java.util.Collections;
import java.util.List;

public class DelimitedTextualConfiguration extends AbstractTextualConfiguration<DelimitedTextualFieldConfiguration, DelimitedTextualFieldSpec, DelimitedTextualSpec<DelimitedTextualFieldSpec>>
{
	public DelimitedTextualConfiguration()
	{
	}

	private String closeQuoteValue;
	private String fieldDelimiter;
	private String openQuoteValue;

	public String getCloseQuoteValue()
	{
		return this.closeQuoteValue;
	}

	public void setCloseQuoteValue(String closeQuoteValue)
	{
		this.closeQuoteValue = closeQuoteValue;
	}

	public String getFieldDelimiter()
	{
		return this.fieldDelimiter;
	}

	public void setFieldDelimiter(String fieldDelimiter)
	{
		this.fieldDelimiter = fieldDelimiter;
	}

	public String getOpenQuoteValue()
	{
		return this.openQuoteValue;
	}

	public void setOpenQuoteValue(String openQuoteValue)
	{
		this.openQuoteValue = openQuoteValue;
	}

	@Override
	public DelimitedTextualSpec<DelimitedTextualFieldSpec> mapToSpec()
	{
		DelimitedTextualSpec<DelimitedTextualFieldSpec> delimitedTextualSpec;
		DelimitedTextualSpecImpl temp;

		delimitedTextualSpec = temp = new DelimitedTextualSpecImpl();

		temp.setCloseQuoteValue(this.getCloseQuoteValue());
		temp.setFieldDelimiter(this.getFieldDelimiter());
		temp.setFirstRecordHeader(this.isFirstRecordHeader());
		temp.setLastRecordHeader(this.isLastRecordFooter());
		temp.setOpenQuoteValue(this.getOpenQuoteValue());
		temp.setRecordDelimiter(this.getRecordDelimiter());

		for (DelimitedTextualFieldConfiguration delimitedTextualFieldConfiguration : this.getTextualHeaderConfigs())
		{
			DelimitedTextualFieldSpecImpl delimitedTextualFieldSpec;

			if (delimitedTextualFieldConfiguration == null)
				continue;

			delimitedTextualFieldSpec = new DelimitedTextualFieldSpecImpl();
			delimitedTextualFieldSpec.setFieldTitle(delimitedTextualFieldConfiguration.getFieldTitle());
			delimitedTextualFieldSpec.setFieldIdentity(delimitedTextualFieldConfiguration.isFieldIdentity());
			delimitedTextualFieldSpec.setFieldRequired(delimitedTextualFieldConfiguration.isFieldRequired());
			delimitedTextualFieldSpec.setFieldType(delimitedTextualFieldConfiguration.getFieldType());
			delimitedTextualFieldSpec.setFieldOrdinal(delimitedTextualFieldConfiguration.getFieldOrdinal());

			//delimitedTextualSpec.getTextualHeaderSpecs().add(delimitedTextualFieldSpec);
		}

		return delimitedTextualSpec;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages = Collections.emptyList();
		return messages;
	}

	/*public DelimitedTextualSpec toSpec(DelimitedTextualConfiguration delimitedTextSpecConfiguration)
	{
		IList<IDelimitedTextualFieldSpec> delimitedTextFieldSpecs;
		DelimitedTextualSpec delimitedTextualSpec;

		if ((object)delimitedTextSpecConfiguration == null)
			throw new ArgumentNullException(nameof(delimitedTextSpecConfiguration));

		delimitedTextualSpec = new DelimitedTextualSpec()
		{
			CloseQuoteValue = delimitedTextSpecConfiguration.CloseQuoteValue,
			FieldDelimiter = delimitedTextSpecConfiguration.FieldDelimiter,
			IsFirstRecordHeader = delimitedTextSpecConfiguration.FirstRecordIsHeader ?? false,
			IsLastRecordFooter = delimitedTextSpecConfiguration.LastRecordIsFooter ?? false,
			OpenQuoteValue = delimitedTextSpecConfiguration.OpenQuoteValue,
			RecordDelimiter = delimitedTextSpecConfiguration.RecordDelimiter
		};

		delimitedTextFieldSpecs = new List<IDelimitedTextualFieldSpec>();

		foreach (DelimitedTextualFieldConfiguration delimitedTextHeaderFieldConfiguration in delimitedTextSpecConfiguration.DelimitedTextHeaderFieldConfigurations)
		{
			DelimitedTextualFieldSpec delimitedTextualFieldSpec;

			delimitedTextualFieldSpec = new DelimitedTextualFieldSpec()
			{
				FieldTitle = delimitedTextHeaderFieldConfiguration.FieldName,
				IsFieldIdentity = delimitedTextHeaderFieldConfiguration.IsKeyComponent ?? false,
				FieldType = delimitedTextHeaderFieldConfiguration.GetFieldType(),
				IsFieldRequired = delimitedTextHeaderFieldConfiguration.IsOptional ?? false,
				FieldOrdinal = delimitedTextFieldSpecs.Count
			};

			delimitedTextFieldSpecs.Add(delimitedTextualFieldSpec);
		}

		delimitedTextualSpec.DelimitedTextHeaderSpecs = delimitedTextFieldSpecs;

		return delimitedTextualSpec;
	}*/
}

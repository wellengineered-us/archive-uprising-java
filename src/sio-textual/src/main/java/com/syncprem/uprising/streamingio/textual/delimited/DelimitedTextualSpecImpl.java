/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.delimited;

import com.syncprem.uprising.streamingio.textual.AbstractTextualSpec;

public class DelimitedTextualSpecImpl extends AbstractTextualSpec<DelimitedTextualFieldSpec> implements DelimitedTextualSpec<DelimitedTextualFieldSpec>
{
	public DelimitedTextualSpecImpl()
	{
	}

	private String closeQuoteValue;
	private String fieldDelimiter;
	private String openQuoteValue;

	@Override
	public String getCloseQuoteValue()
	{
		return this.closeQuoteValue;
	}

	public void setCloseQuoteValue(String closeQuoteValue)
	{
		this.closeQuoteValue = closeQuoteValue;
	}

	@Override
	public String getFieldDelimiter()
	{
		return this.fieldDelimiter;
	}

	public void setFieldDelimiter(String fieldDelimiter)
	{
		this.fieldDelimiter = fieldDelimiter;
	}

	@Override
	public String getOpenQuoteValue()
	{
		return this.openQuoteValue;
	}

	public void setOpenQuoteValue(String openQuoteValue)
	{
		this.openQuoteValue = openQuoteValue;
	}
}

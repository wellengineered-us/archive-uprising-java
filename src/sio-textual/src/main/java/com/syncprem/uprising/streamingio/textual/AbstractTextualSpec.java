/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTextualSpec<TTextualFieldSpec extends TextualFieldSpec> implements TextualSpec<TTextualFieldSpec>
{
	protected AbstractTextualSpec()
	{
		this(new ArrayList<>(), new ArrayList<>());
	}

	protected AbstractTextualSpec(List<TTextualFieldSpec> textualHeaderSpecs, List<TTextualFieldSpec> textualFooterSpecs)
	{
		if (textualHeaderSpecs == null)
			throw new ArgumentNullException("textualHeaderSpecs");

		if (textualFooterSpecs == null)
			throw new ArgumentNullException("textualFooterSpecs");

		this.textualHeaderSpecs = textualHeaderSpecs;
		this.textualFooterSpecs = textualFooterSpecs;
	}

	private final List<TTextualFieldSpec> textualFooterSpecs;
	private final List<TTextualFieldSpec> textualHeaderSpecs;
	private boolean isFirstRecordHeader;
	private boolean isLastRecordHeader;
	private String recordDelimiter;

	@Override
	public String getRecordDelimiter()
	{
		return this.recordDelimiter;
	}

	public void setRecordDelimiter(String recordDelimiter)
	{
		this.recordDelimiter = recordDelimiter;
	}

	@Override
	public List<TTextualFieldSpec> getTextualFooterSpecs()
	{
		return this.textualFooterSpecs;
	}

	@Override
	public List<TTextualFieldSpec> getTextualHeaderSpecs()
	{
		return this.textualHeaderSpecs;
	}

	@Override
	public boolean isFirstRecordHeader()
	{
		return this.isFirstRecordHeader;
	}

	public void setFirstRecordHeader(boolean isFirstRecordHeader)
	{
		this.isFirstRecordHeader = isFirstRecordHeader;
	}

	@Override
	public boolean isLastRecordFooter()
	{
		return this.isLastRecordHeader;
	}

	public void setLastRecordHeader(boolean isLastRecordHeader)
	{
		this.isLastRecordHeader = isLastRecordHeader;
	}

	@Override
	public void assertValid()
	{
		// TODO
	}
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.infrastructure.polyfills.MessageImpl;
import com.syncprem.uprising.infrastructure.polyfills.Severity;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.streamingio.textual.TextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.TextualSpec;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTextualFileConnectorSpecificConfiguration<
		TTextualFieldSpec extends TextualFieldSpec,
		TTextualSpec extends TextualSpec<TTextualFieldSpec>,
		TTextualFieldConfiguration extends AbstractTextualFieldConfiguration,
		TTextualConfiguration
				extends AbstractTextualConfiguration<TTextualFieldConfiguration, TTextualFieldSpec, TTextualSpec>> extends ComponentSpecificConfiguration
{
	protected AbstractTextualFileConnectorSpecificConfiguration()
	{
	}

	private TTextualConfiguration textualConfiguration;
	private String textualFilePath;

	public TTextualConfiguration getTextualConfiguration()
	{
		return this.textualConfiguration;
	}

	public void setTextualConfiguration(TTextualConfiguration textualConfiguration)
	{
		this.ensureParentOnSetter(this.textualConfiguration, textualConfiguration);
		this.textualConfiguration = textualConfiguration;
	}

	public String getTextualFilePath()
	{
		return this.textualFilePath;
	}

	public void setTextualFilePath(String textualFilePath)
	{
		this.textualFilePath = textualFilePath;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (Utils.isNullOrEmptyString(this.getTextualFilePath()))
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s connector textual file path is required.", context), Severity.ERROR));

		return messages;
	}
}

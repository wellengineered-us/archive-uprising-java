/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.configuration.AbstractConfigurationObject;
import com.syncprem.uprising.infrastructure.configuration.ConfigurationObjectCollectionImpl;
import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.streamingio.textual.TextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.TextualSpec;

import java.util.Collections;
import java.util.List;

public abstract class AbstractTextualConfiguration<TTextualFieldConfiguration extends AbstractTextualFieldConfiguration,
		TTextualFieldSpec extends TextualFieldSpec,
		TTextualSpec extends TextualSpec<TTextualFieldSpec>> extends AbstractConfigurationObject
{
	protected AbstractTextualConfiguration()
	{
		this.textualHeaderConfigs = new ConfigurationObjectCollectionImpl<>(this);
		this.textualFooterConfigs = new ConfigurationObjectCollectionImpl<>(this);
	}

	protected AbstractTextualConfiguration(ConfigurationObjectCollectionImpl<TTextualFieldConfiguration> textualHeaderConfigs, ConfigurationObjectCollectionImpl<TTextualFieldConfiguration> textualFooterConfigs)
	{
		this.textualHeaderConfigs = textualHeaderConfigs;
		this.textualFooterConfigs = textualFooterConfigs;
	}

	private final ConfigurationObjectCollectionImpl<TTextualFieldConfiguration> textualFooterConfigs;
	private final ConfigurationObjectCollectionImpl<TTextualFieldConfiguration> textualHeaderConfigs;
	private Boolean isFirstRecordHeader;
	private Boolean isLastRecordFooter;
	private String recordDelimiter;

	public String getRecordDelimiter()
	{
		return this.recordDelimiter;
	}

	public void setRecordDelimiter(String recordDelimiter)
	{
		this.recordDelimiter = recordDelimiter;
	}

	public ConfigurationObjectCollectionImpl<TTextualFieldConfiguration> getTextualFooterConfigs()
	{
		return this.textualFooterConfigs;
	}

	public ConfigurationObjectCollectionImpl<TTextualFieldConfiguration> getTextualHeaderConfigs()
	{
		return this.textualHeaderConfigs;
	}

	public void setIsFirstRecordHeader(Boolean isFirstRecordHeader)
	{
		this.isFirstRecordHeader = isFirstRecordHeader;
	}

	public void setIsLastRecordFooter(Boolean isLastRecordFooter)
	{
		this.isLastRecordFooter = isLastRecordFooter;
	}

	public Boolean isFirstRecordHeader()
	{
		return this.isFirstRecordHeader;
	}

	public Boolean isLastRecordFooter()
	{
		return this.isLastRecordFooter;
	}

	public abstract TTextualSpec mapToSpec();

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages = Collections.emptyList();
		return messages;
	}
}

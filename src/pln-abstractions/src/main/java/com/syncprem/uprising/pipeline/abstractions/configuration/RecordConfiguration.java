/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.configuration;

import com.syncprem.uprising.infrastructure.configuration.ConfigurationObjectCollectionImpl;
import com.syncprem.uprising.infrastructure.polyfills.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class RecordConfiguration extends ComponentConfiguration
{
	public RecordConfiguration()
	{
		this.fieldConfigurations = new ConfigurationObjectCollectionImpl<FieldConfiguration>(this);
	}

	public RecordConfiguration(ConfigurationObjectCollectionImpl<FieldConfiguration> fieldConfigurations)
	{
		if (fieldConfigurations == null)
			throw new ArgumentNullException("fieldConfigurations");

		this.fieldConfigurations = fieldConfigurations;
	}

	private final ConfigurationObjectCollectionImpl<FieldConfiguration> fieldConfigurations;

	public ConfigurationObjectCollectionImpl<FieldConfiguration> getFieldConfigurations()
	{
		return this.fieldConfigurations;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages;

		messages = new ArrayList<>();

		if (this.getFieldConfigurations() == null)
			messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("Field configurations are required."), Severity.ERROR));
		else
		{
			MessageImpl.addRange(messages, this.getFieldConfigurations().validateAll("Field"));

			final Map<String, List<FieldConfiguration>> orderedGroups = this.getFieldConfigurations()
					.stream()
					.sorted(Comparator.comparing(FieldConfiguration::getFieldName))
					.collect(groupingBy(FieldConfiguration::getFieldName));

			// check for duplicate fields
			for (Map.Entry<String, List<FieldConfiguration>> orderedGroup : orderedGroups.entrySet())
			{
				if (orderedGroup == null)
					continue;

				if (orderedGroup.getValue().size() > 1)
					messages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("%s has a duplicate field name defined: '%s'.", context, orderedGroup.getKey()), Severity.ERROR));
			}
		}

		return messages;
	}
}

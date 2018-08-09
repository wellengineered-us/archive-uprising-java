/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

import java.util.List;

/**
 * Represents a message with a category, description, and severity.
 */
public final class MessageImpl implements Message
{
	public MessageImpl(String category, String description, Severity severity)
	{
		if (category == null)
			throw new ArgumentNullException("category");

		if (description == null)
			throw new ArgumentNullException("description");

		if (severity == null)
			throw new ArgumentNullException("severity");

		this.category = category;
		this.description = description;
		this.severity = severity;
	}

	private final String category;
	private final String description;
	private final Severity severity;

	@Override
	public String getCategory()
	{
		return category;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public Severity getSeverity()
	{
		return severity;
	}

	public static void addRange(List<Message> target, Iterable<Message> source)
	{
		if (target == null)
			throw new ArgumentNullException("target");

		if (source == null)
			throw new ArgumentNullException("source");

		for (Message message : source) // null OK
			target.add(message);
	}
}

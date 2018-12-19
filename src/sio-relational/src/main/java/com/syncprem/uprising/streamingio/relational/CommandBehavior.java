/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

public enum CommandBehavior
{
	/**
	 * With data, multiple results, may affect database.
	 */
	DEFAULT(0),

	/**
	 *
	 */
	CLOSE_CONNECTION(32),

	/**
	 * Column info + primary key information (if available).
	 */
	KEY_INFO(4),

	/**
	 * Column info, no data, no effect on database.
	 */
	SCHEMA_ONLY(2),

	/**
	 *
	 */
	SEQUENTIAL_ACCESS(16),

	/**
	 * With data, force single result, may affect database.
	 */
	SINGLE_RESULT(1),

	/**
	 * With data, hint single row and single result, may affect database - doesn't apply to child(chapter) results.
	 */
	SINGLE_ROW(8);

	CommandBehavior(final Integer value)
	{
		this.value = value;
	}

	private Integer value;

	public boolean hasFlag(CommandBehavior flag)
	{
		if (flag == null)
			throw new ArgumentNullException("flag");

		return (this.value & flag.value) == flag.value;
	}
}

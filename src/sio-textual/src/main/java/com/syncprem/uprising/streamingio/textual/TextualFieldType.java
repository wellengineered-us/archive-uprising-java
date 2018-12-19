/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.math.BigDecimal;
import java.util.Date;

public enum TextualFieldType
{
	UNKNOWN(0),
	TEXT(1),
	CURRENCY(2),
	DATETIME(3),
	LOGICAL(4),
	NUMBER(5);

	TextualFieldType(int value)
	{
		this.value = value;
	}

	private final int value;

	public Class<?> ToJvmClass()
	{
		switch (Utils.getValueOrDefault(this, TextualFieldType.UNKNOWN))
		{
			case TEXT:
				return String.class;
			case CURRENCY:
				return BigDecimal.class;
			case DATETIME:
				return Date.class;
			case LOGICAL:
				return Boolean.class;
			case NUMBER:
				return Double.class;
			default:
				return Object.class;
		}
	}
}

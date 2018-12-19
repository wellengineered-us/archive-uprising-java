/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.Utils;

public enum HttpScope
{
	UNKNOWN(Utils.EMPTY_STRING),

	COLLECTION("COLLECTION"),
	RESOURCE("RESOURCE"),
	ANY("ANY");

	HttpScope(final String value)
	{
		this.value = value;
	}

	private String value;
}

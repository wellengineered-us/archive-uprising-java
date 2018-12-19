/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.Utils;

public enum HttpMethod
{
	UNKNOWN(Utils.EMPTY_STRING),

	POST("POST"),
	GET("GET"),
	PUT("PUT"),
	PATCH("PATCH"),
	DELETE("DELETE"),

	HEAD("HEAD"),
	TRACE("TRACE"),
	OPTIONS("OPTIONS"),
	CONNECT("CONNECT");

	HttpMethod(final String value)
	{
		this.value = value;
	}

	private String value;
}

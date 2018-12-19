/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import java.net.HttpURLConnection;

public enum HttpStatus
{
	UNKNOWN(0),

	HTTP_OK(HttpURLConnection.HTTP_OK),
	HTTP_CREATED(HttpURLConnection.HTTP_CREATED),
	HTTP_ACCEPTED(HttpURLConnection.HTTP_ACCEPTED),
	HTTP_NOT_AUTHORITATIVE(HttpURLConnection.HTTP_NOT_AUTHORITATIVE),
	HTTP_NO_CONTENT(HttpURLConnection.HTTP_NO_CONTENT),
	HTTP_RESET(HttpURLConnection.HTTP_RESET),
	HTTP_PARTIAL(HttpURLConnection.HTTP_PARTIAL),

	HTTP_MULT_CHOICE(HttpURLConnection.HTTP_MULT_CHOICE),
	HTTP_MOVED_PERM(HttpURLConnection.HTTP_MOVED_PERM),
	HTTP_MOVED_TEMP(HttpURLConnection.HTTP_MOVED_TEMP),
	HTTP_SEE_OTHER(HttpURLConnection.HTTP_SEE_OTHER),
	HTTP_NOT_MODIFIED(HttpURLConnection.HTTP_NOT_MODIFIED),
	HTTP_USE_PROXY(HttpURLConnection.HTTP_USE_PROXY),

	HTTP_BAD_REQUEST(HttpURLConnection.HTTP_BAD_REQUEST),
	HTTP_UNAUTHORIZED(HttpURLConnection.HTTP_UNAUTHORIZED),
	HTTP_PAYMENT_REQUIRED(HttpURLConnection.HTTP_PAYMENT_REQUIRED),
	HTTP_FORBIDDEN(HttpURLConnection.HTTP_FORBIDDEN),
	HTTP_NOT_FOUND(HttpURLConnection.HTTP_NOT_FOUND),
	HTTP_BAD_METHOD(HttpURLConnection.HTTP_BAD_METHOD),
	HTTP_NOT_ACCEPTABLE(HttpURLConnection.HTTP_NOT_ACCEPTABLE),
	HTTP_PROXY_AUTH(HttpURLConnection.HTTP_PROXY_AUTH),
	HTTP_CLIENT_TIMEOUT(HttpURLConnection.HTTP_CLIENT_TIMEOUT),
	HTTP_CONFLICT(HttpURLConnection.HTTP_CONFLICT),
	HTTP_GONE(HttpURLConnection.HTTP_GONE),
	HTTP_LENGTH_REQUIRED(HttpURLConnection.HTTP_LENGTH_REQUIRED),
	HTTP_PRECON_FAILED(HttpURLConnection.HTTP_PRECON_FAILED),
	HTTP_ENTITY_TOO_LARGE(HttpURLConnection.HTTP_ENTITY_TOO_LARGE),
	HTTP_REQ_TOO_LONG(HttpURLConnection.HTTP_REQ_TOO_LONG),
	HTTP_UNSUPPORTED_TYPE(HttpURLConnection.HTTP_UNSUPPORTED_TYPE),

	HTTP_INTERNAL_ERROR(HttpURLConnection.HTTP_INTERNAL_ERROR),
	HTTP_NOT_IMPLEMENTED(HttpURLConnection.HTTP_NOT_IMPLEMENTED),
	HTTP_BAD_GATEWAY(HttpURLConnection.HTTP_BAD_GATEWAY),
	HTTP_UNAVAILABLE(HttpURLConnection.HTTP_UNAVAILABLE),
	HTTP_GATEWAY_TIMEOUT(HttpURLConnection.HTTP_GATEWAY_TIMEOUT),
	HTTP_VERSION(HttpURLConnection.HTTP_VERSION);

	HttpStatus(final int value)
	{
		this.value = value;
	}

	private int value;

	public int getValue()
	{
		return this.value;
	}

	public static HttpStatus from(int value)
	{
		for (HttpStatus item : HttpStatus.values())
		{
			if (item.value == value)
				return item;
		}

		return null;
	}
}

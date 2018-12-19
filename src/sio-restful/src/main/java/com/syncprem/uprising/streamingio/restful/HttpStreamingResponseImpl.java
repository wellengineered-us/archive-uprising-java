/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.net.HttpURLConnection;
import java.util.Map;

public class HttpStreamingResponseImpl extends AbstractLifecycle<Exception, Exception> implements HttpStreamingResponse
{
	public HttpStreamingResponseImpl(HttpURLConnection httpUrlConnection, HttpStreamingRequest httpStreamingRequest,
									 String responseVersion, HttpStatus responseStatus, String responseMessage, Map<String, Iterable<String>> responseHeaders, HttpStreamingContent responseContent)
	{
		if (httpUrlConnection == null)
			throw new ArgumentNullException("httpUrlConnection");

		if (httpStreamingRequest == null)
			throw new ArgumentNullException("httpStreamingRequest");

		if (responseVersion == null)
			throw new ArgumentNullException("responseVersion");

		if (responseStatus == null)
			throw new ArgumentNullException("responseStatus");

		if (responseMessage == null)
			throw new ArgumentNullException("responseMessage");

		if (responseHeaders == null)
			throw new ArgumentNullException("responseHeaders");

		if (responseContent == null)
			throw new ArgumentNullException("responseContent");

		this.httpUrlConnection = httpUrlConnection;
		this.httpStreamingRequest = httpStreamingRequest;
		this.responseVersion = responseVersion;
		this.responseStatus = responseStatus;
		this.responseMessage = responseMessage;
		this.responseHeaders = responseHeaders;
		this.responseContent = responseContent;
	}

	private final HttpStreamingRequest httpStreamingRequest;
	private final HttpURLConnection httpUrlConnection;
	private final HttpStreamingContent responseContent;
	private final Map<String, Iterable<String>> responseHeaders;
	private final String responseMessage;
	private final HttpStatus responseStatus;
	private final String responseVersion;

	@Override
	public HttpStreamingRequest getHttpStreamingRequest()
	{
		return this.httpStreamingRequest;
	}

	private HttpURLConnection getHttpUrlConnection()
	{
		return this.httpUrlConnection;
	}

	@Override
	public HttpStreamingContent getResponseContent()
	{
		return this.responseContent;
	}

	@Override
	public Map<String, Iterable<String>> getResponseHeaders()
	{
		return this.responseHeaders;
	}

	@Override
	public String getResponseMessage()
	{
		return this.responseMessage;
	}

	@Override
	public HttpStatus getResponseStatus()
	{
		return this.responseStatus;
	}

	@Override
	public String getResponseVersion()
	{
		return this.responseVersion;
	}

	public boolean isRestfulCongruentSuccessResult()
	{
		// ensureRestfulCongruentSuccessResponse()
		System.out.println(String.format("[%s@%s %s %s@%s \"%s\" %s]", this.getHttpStreamingRequest().getRequestMethod(),
				this.getHttpStreamingRequest().getRequestScope(),
				this.getHttpStreamingRequest().getRequestUri(),
				this.getResponseStatus(),
				this.getResponseStatus() != null ? this.getResponseStatus().getValue() : null,
				this.getResponseMessage(),
				this.getHttpUrlConnection().getHeaderField("Location")
		));

		switch (this.getHttpStreamingRequest().getRequestScope())
		{
			case COLLECTION:
				switch (this.getHttpStreamingRequest().getRequestMethod())
				{
					case POST:
						switch (this.getResponseStatus())
						{
							case HTTP_CREATED: // + location header
								if (!Utils.isNullOrEmptyString(this.getHttpUrlConnection().getHeaderField("Location")))
									return true;
						}
						break;
					case GET:
						switch (this.getResponseStatus())
						{
							case HTTP_OK:
								return true;
						}
						break;
					case PUT:
					case PATCH:
					case DELETE:
					default:
						return false; // contraindicated
				}
				break;
			case RESOURCE:
				switch (this.getHttpStreamingRequest().getRequestMethod())
				{
					case POST:
						return false; // contraindicated
					case GET:
						switch (this.getResponseStatus())
						{
							case HTTP_OK:
								return true;
						}
						break;
					case PUT:
					case PATCH:
					case DELETE:
						switch (this.getResponseStatus())
						{
							case HTTP_OK:
							case HTTP_NO_CONTENT:
								return true;
						}
						break;
					default:
						return false;
				}
				break;
			default:
				return false;
		}

		return false;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			if (this.getHttpUrlConnection() != null)
				this.getHttpUrlConnection().disconnect();
		}
	}
}

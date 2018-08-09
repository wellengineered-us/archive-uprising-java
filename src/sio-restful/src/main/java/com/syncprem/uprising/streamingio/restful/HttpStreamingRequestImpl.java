/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;

import java.net.URL;
import java.util.Map;

public class HttpStreamingRequestImpl extends AbstractLifecycle<Exception, Exception> implements HttpStreamingRequest
{
	public HttpStreamingRequestImpl(String requestVersion, HttpMethod requestMethod, URL requestUri, Map<String, Iterable<String>> requestHeaders, HttpScope requestScope, HttpStreamingContent requestContent)
	{
		if (requestVersion == null)
			throw new ArgumentNullException("requestVersion");

		if (requestMethod == null)
			throw new ArgumentNullException("httpMethod");

		if (requestUri == null)
			throw new ArgumentNullException("requestUri");

		if (requestHeaders == null)
			throw new ArgumentNullException("requestHeaders");

		if (requestScope == null)
			throw new ArgumentNullException("requestScope");

		if (requestContent == null)
			throw new ArgumentNullException("requestContent");

		this.requestVersion = requestVersion;
		this.requestMethod = requestMethod;
		this.requestUri = requestUri;
		this.requestHeaders = requestHeaders;
		this.requestScope = requestScope;
		this.requestContent = requestContent;
	}

	private final HttpStreamingContent requestContent;
	private final Map<String, Iterable<String>> requestHeaders;
	private final HttpMethod requestMethod;
	private final HttpScope requestScope;
	private final URL requestUri;
	private final String requestVersion;

	@Override
	public HttpStreamingContent getRequestContent()
	{
		return this.requestContent;
	}

	@Override
	public Map<String, Iterable<String>> getRequestHeaders()
	{
		return this.requestHeaders;
	}

	@Override
	public HttpMethod getRequestMethod()
	{
		return this.requestMethod;
	}

	@Override
	public HttpScope getRequestScope()
	{
		return this.requestScope;
	}

	@Override
	public URL getRequestUri()
	{
		return this.requestUri;
	}

	@Override
	public String getRequestVersion()
	{
		return this.requestVersion;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		// do nothing
	}
}

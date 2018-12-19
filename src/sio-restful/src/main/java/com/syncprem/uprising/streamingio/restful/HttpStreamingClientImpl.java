/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public final class HttpStreamingClientImpl extends AbstractLifecycle<Exception, Exception> implements HttpStreamingClient
{
	public HttpStreamingClientImpl()
	{
		this(new DisposableList<>());
	}

	public HttpStreamingClientImpl(DisposableList<HttpStreamingResponse> disposables)
	{
		if (disposables == null)
			throw new ArgumentNullException("disposables");

		this.disposables = disposables;
	}

	private final DisposableList<HttpStreamingResponse> disposables;

	private DisposableList<HttpStreamingResponse> getDisposables()
	{
		return this.disposables;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (creating)
		{
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			if (this.getDisposables() != null)
				this.getDisposables().dispose();
		}
	}

	@Override
	public HttpStreamingResponse send(HttpStreamingRequest httpStreamingRequest) throws IOException
	{
		// THANKS TO:
		// https://www.tbray.org/ongoing/When/201x/2012/01/17/HttpURLConnection
		// For detail on how this contraption should function...
		HttpURLConnection httpUrlConnection;

		boolean hasRequestOutputStream;
		boolean hasResponseInputStream;

		final int CHUNK_LEN = 1024;

		if (httpStreamingRequest == null)
			throw new ArgumentNullException("httpStreamingRequest");

		// NOTE: no network I/O here ;)
		httpUrlConnection = (HttpURLConnection) httpStreamingRequest.getRequestUri().openConnection();

		final Map<String, Iterable<String>> requestHeaders = httpStreamingRequest.getRequestHeaders();

		if (requestHeaders != null)
		{
			for (Map.Entry<String, Iterable<String>> entry : requestHeaders.entrySet())
			{
				if (entry == null)
					continue;

				if (entry.getKey() == null)
					continue;

				if (entry.getValue() == null)
					continue;

				// NOTE: no network I/O here ;)
				for (String value : entry.getValue())
				{
					if (value == null)
						continue;

					httpUrlConnection.addRequestProperty(entry.getKey(), value);
				}
			}
		}

		switch (httpStreamingRequest.getRequestMethod())
		{
			case GET:
			case DELETE:
				hasRequestOutputStream = false;
				hasResponseInputStream = true;
				break;
			case POST:
			case PUT:
			case PATCH:
				hasRequestOutputStream = true;
				hasResponseInputStream = true;
				break;
			case HEAD:
			case TRACE:
			case OPTIONS:
			case CONNECT:
				hasRequestOutputStream = false;
				hasResponseInputStream = true;
				break;
			default:
				throw new InvalidOperationException("Unsupported HTTP method.");
		}

		// NOTE: no network I/O here ;)
		httpUrlConnection.setRequestMethod(httpStreamingRequest.getRequestMethod().toString());

		// NOTE: no network I/O here ;)
		httpUrlConnection.setDoOutput(hasRequestOutputStream);
		httpUrlConnection.setDoInput(hasResponseInputStream);

		// --------------

		// get request output stream
		if (hasRequestOutputStream)
		{
			final Long requestContentLength = httpStreamingRequest.getRequestContent().getLength();

			// NOTE: no network I/O here ;)
			if (requestContentLength != null && requestContentLength > 0L)
				httpUrlConnection.setFixedLengthStreamingMode((long) requestContentLength);
			else
				httpUrlConnection.setChunkedStreamingMode(CHUNK_LEN);

			// NOTE: actual network I/O here !!!
			final OutputStream requestOutputStream = httpUrlConnection.getOutputStream();

			failFastOnlyWhen(httpStreamingRequest.getRequestContent() == null, "httpStreamingRequest.getRequestContent() == null");

			// now we can send the body content...
			// NOTE: actual network I/O here !!!
			httpStreamingRequest.getRequestContent().copyTo(requestOutputStream);
		}

		// --------------

		// NOW you can look at the status, message, and response headers.
		final int responseCode = httpUrlConnection.getResponseCode();
		final String responseVersion = Utils.EMPTY_STRING;
		final String responseMessage = httpUrlConnection.getResponseMessage();
		final Map<String, List<String>> headerFields = httpUrlConnection.getHeaderFields();

		final HttpStatus responseStatus = HttpStatus.from(responseCode);
		final Map<String, Iterable<String>> responseHeaders = new LinkedHashMap<>();

		if (headerFields != null)
		{
			for (Map.Entry<String, List<String>> entry : headerFields.entrySet())
			{
				if (entry == null)
					continue;

				if (entry.getKey() == null)
					continue;

				if (entry.getValue() == null)
					continue;

				responseHeaders.put(entry.getKey(), entry.getValue());
			}
		}

		HttpStreamingContent responseContent = null;
		if (hasResponseInputStream)
		{
			// get response input stream
			InputStream responseInputStream;
			try
			{
				// NOTE: no network I/O here ;) YES, true story.
				responseInputStream = httpUrlConnection.getInputStream();
			}
			catch (IOException ioe)
			{
				// NOTE: no network I/O here ;) YES, true story.
				responseInputStream = httpUrlConnection.getErrorStream();
			}

			if (responseInputStream == null)
				responseContent = new EmptyStreamContent();
			else
				responseContent = new PullOutputStreamContent(responseInputStream);
		}

		final HttpStreamingResponse httpStreamingResponse = new HttpStreamingResponseImpl(httpUrlConnection, httpStreamingRequest,
				responseVersion, responseStatus, responseMessage, responseHeaders, responseContent);

		this.getDisposables().add(httpStreamingResponse); // track disposables
		return httpStreamingResponse;
	}
}

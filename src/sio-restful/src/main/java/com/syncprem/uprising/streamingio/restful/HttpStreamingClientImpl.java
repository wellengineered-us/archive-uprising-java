/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.AbstractLifecycle;
import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.DisposableList;
import com.syncprem.uprising.infrastructure.polyfills.Utils;

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
		HttpURLConnection httpUrlConnection;
		boolean hasOutput;
		final boolean hasInput = true;

		final int CHUNK_LEN = 1024;

		if (httpStreamingRequest == null)
			throw new ArgumentNullException("httpStreamingRequest");

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

				for (String value : entry.getValue())
					httpUrlConnection.addRequestProperty(entry.getKey(), value);
			}
		}

		switch (httpStreamingRequest.getRequestMethod())
		{
			case POST:
			case PUT:
			case PATCH:
				hasOutput = true;
				break;
			default:
				hasOutput = false;
				break;
		}

		httpUrlConnection.setRequestMethod(httpStreamingRequest.getRequestMethod().toString());
		httpUrlConnection.setDoOutput(hasOutput);

		// --------------

		if (hasInput)
			httpUrlConnection.setDoInput(true);

		if (hasOutput)
		{
			final Long requestContentLength = httpStreamingRequest.getRequestContent().getLength();

			if (requestContentLength != null)
				httpUrlConnection.setFixedLengthStreamingMode((long) requestContentLength);
			else
				httpUrlConnection.setChunkedStreamingMode(CHUNK_LEN);

			final OutputStream requestOutputStream = httpUrlConnection.getOutputStream();

			failFastOnlyWhen(httpStreamingRequest.getRequestContent() == null, "httpStreamingRequest.getRequestContent() == null");
			httpStreamingRequest.getRequestContent().copyTo(requestOutputStream);
		}

		// --------------

		final int responseCode = httpUrlConnection.getResponseCode();
		final String responseVersion = Utils.EMPTY_STRING;
		final String responseMessage = httpUrlConnection.getResponseMessage();
		final HttpStatus responseStatus = HttpStatus.from(responseCode);
		final Map<String, Iterable<String>> responseHeaders = new LinkedHashMap<>();

		InputStream responseInputStream;
		try
		{
			responseInputStream = httpUrlConnection.getInputStream();
		}
		catch (IOException ioe)
		{
			responseInputStream = httpUrlConnection.getErrorStream();
		}

		final HttpStreamingContent responseContent = new PullOutputStreamContent(responseInputStream);

		final Map<String, List<String>> headerFields = httpUrlConnection.getHeaderFields();

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

		final HttpStreamingResponse httpStreamingResponse = new HttpStreamingResponseImpl(httpUrlConnection, httpStreamingRequest,
				responseVersion, responseStatus, responseMessage, responseHeaders, responseContent);

		this.getDisposables().add(httpStreamingResponse); // track disposables
		return httpStreamingResponse;
	}
}

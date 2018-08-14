/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.net.URL;
import java.util.Map;

public interface HttpStreamingRequest extends Creatable, Disposable
{
	HttpStreamingContent getRequestContent();

	Map<String, Iterable<String>> getRequestHeaders();

	HttpMethod getRequestMethod();

	HttpScope getRequestScope();

	URL getRequestUri();

	String getRequestVersion();
}

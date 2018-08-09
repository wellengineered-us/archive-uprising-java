/*
	Copyright �2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.util.Map;

public interface HttpStreamingResponse extends Creatable, Disposable
{
	HttpStreamingRequest getHttpStreamingRequest();

	HttpStreamingContent getResponseContent();

	Map<String, Iterable<String>> getResponseHeaders();

	String getResponseMessage();

	HttpStatus getResponseStatus();

	String getResponseVersion();

	boolean isRestfulCongruentSuccessResult();
}

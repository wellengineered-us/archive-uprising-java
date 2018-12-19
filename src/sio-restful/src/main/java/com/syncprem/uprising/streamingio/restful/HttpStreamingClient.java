/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.io.IOException;

public interface HttpStreamingClient extends Creatable, Disposable
{
	HttpStreamingResponse send(HttpStreamingRequest httpStreamingRequest) throws IOException;
}

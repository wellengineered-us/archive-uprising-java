/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.restful;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpStreamingContent extends Creatable, Disposable
{
	Long getLength() throws IOException;

	void copyTo(OutputStream outputStream) throws IOException;

	InputStream readFrom() throws IOException;
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.proxywrappers.strategies;

import java.io.IOException;
import java.io.OutputStream;

public interface IOWriteWrapperStrategy
{
	OutputStream wrap(OutputStream outputStream) throws IOException;
}
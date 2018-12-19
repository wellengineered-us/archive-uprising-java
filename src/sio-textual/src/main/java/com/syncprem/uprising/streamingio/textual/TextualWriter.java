/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.streamingio.primitives.Payload;

import java.io.IOException;
import java.io.Writer;

public interface TextualWriter<TTextualFieldSpec extends TextualFieldSpec,
		TTextualSpec extends TextualSpec<? extends TTextualFieldSpec>> extends Creatable, Disposable
{
	Writer getBaseWriter() throws IOException;

	TTextualSpec getTextualSpec();

	void flush() throws IOException;

	void writeFooterRecords(Iterable<TTextualFieldSpec> footers, LifecycleIterator<Payload> records) throws IOException;

	void writeHeaderFields(Iterable<TTextualFieldSpec> headers) throws IOException;

	void writeRecords(LifecycleIterator<Payload> records) throws IOException;
}

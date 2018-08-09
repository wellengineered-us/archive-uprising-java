/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import com.syncprem.uprising.infrastructure.polyfills.Creatable;
import com.syncprem.uprising.infrastructure.polyfills.Disposable;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;

import java.io.IOException;
import java.io.Reader;

public interface TextualReader<TTextualFieldSpec extends TextualFieldSpec,
		TTextualSpec extends TextualSpec<? extends TTextualFieldSpec>> extends Creatable, Disposable
{
	Reader getBaseReader() throws IOException;

	TTextualSpec getTextualSpec();

	LifecycleIterator<TextualStreamingRecord> readFooterRecords(Iterable<TTextualFieldSpec> footers) throws IOException;

	LifecycleIterator<? extends TTextualFieldSpec> readHeaderFields() throws IOException;

	LifecycleIterator<TextualStreamingRecord> readRecords() throws IOException;
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.delimited;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.NotImplementedException;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.textual.AbstractTextualWriter;

import java.io.IOException;
import java.io.Writer;

public class DelimitedTextualWriterImpl extends AbstractTextualWriter<DelimitedTextualFieldSpec, DelimitedTextualSpec<DelimitedTextualFieldSpec>>
		implements DelimitedTextualWriter<DelimitedTextualFieldSpec, DelimitedTextualSpec<DelimitedTextualFieldSpec>>
{
	public DelimitedTextualWriterImpl(Writer baseWriter, DelimitedTextualSpec<DelimitedTextualFieldSpec> delimitedTextualSpec)
	{
		super(baseWriter, delimitedTextualSpec);

		if (delimitedTextualSpec == null)
			throw new ArgumentNullException("delimitedTextualSpec");
	}

	@Override
	public void writeFooterRecords(Iterable<DelimitedTextualFieldSpec> footers, LifecycleIterator<Payload> records) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public void writeHeaderFields(Iterable<DelimitedTextualFieldSpec> headers) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public void writeRecords(LifecycleIterator<Payload> records) throws IOException
	{
		throw new NotImplementedException();
	}
}

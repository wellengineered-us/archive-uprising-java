/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.NotImplementedException;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.textual.AbstractTextualWriter;

import java.io.IOException;
import java.io.Writer;

public class FixedTextualWriterImpl extends AbstractTextualWriter<FixedTextualFieldSpec, FixedTextualSpec<FixedTextualFieldSpec>>
		implements FixedTextualWriter<FixedTextualFieldSpec, FixedTextualSpec<FixedTextualFieldSpec>>
{
	public FixedTextualWriterImpl(Writer baseWriter, FixedTextualSpec<FixedTextualFieldSpec> fixedTextualSpec)
	{
		super(baseWriter, fixedTextualSpec);

		if (fixedTextualSpec == null)
			throw new ArgumentNullException("fixedTextualSpec");
	}

	@Override
	public void writeFooterRecords(Iterable<FixedTextualFieldSpec> footers, LifecycleIterator<Payload> records) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public void writeHeaderFields(Iterable<FixedTextualFieldSpec> headers) throws IOException
	{
		throw new NotImplementedException();
	}

	@Override
	public void writeRecords(LifecycleIterator<Payload> records) throws IOException
	{
		throw new NotImplementedException();
	}
}

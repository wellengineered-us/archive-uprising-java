/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.core.configurations.LinedTextualConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.LinedTextualFieldConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.LinedTextualFileConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualReader;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualReaderImpl;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualSpec;

import java.io.Reader;

public final class LinedTextualSourceConnector extends AbstractTextualSourceConnector
		<LinedTextualFieldConfiguration, LinedTextualConfiguration,
				LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>,
				LinedTextualFileConnectorSpecificConfiguration, LinedTextualReader<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>>
{
	public LinedTextualSourceConnector()
	{
	}

	@Override
	protected LinedTextualReader<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>> createTextualReader(Reader reader, LinedTextualSpec<LinedTextualFieldSpec> textualSpec)
	{
		if (reader == null)
			throw new ArgumentNullException("reader");

		if (textualSpec == null)
			throw new ArgumentNullException("textualSpec");

		return new LinedTextualReaderImpl(reader, textualSpec);
	}

	@Override
	protected Class<LinedTextualFileConnectorSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return LinedTextualFileConnectorSpecificConfiguration.class;
	}
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.pipeline.core.configurations.LinedTextualConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.LinedTextualFieldConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.LinedTextualFileConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualSpec;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualWriter;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualWriterImpl;

import java.io.Writer;

public final class LinedTextualDestinationConnector extends AbstractTextualDestinationConnector
		<LinedTextualFieldConfiguration, LinedTextualConfiguration,
				LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>,
				LinedTextualFileConnectorSpecificConfiguration, LinedTextualWriter<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>>
{
	public LinedTextualDestinationConnector()
	{
	}

	@Override
	protected LinedTextualWriter<LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>> createTextualWriter(Writer reader, LinedTextualSpec<LinedTextualFieldSpec> textualSpec)
	{
		if (reader == null)
			throw new ArgumentNullException("reader");

		if (textualSpec == null)
			throw new ArgumentNullException("textualSpec");

		return new LinedTextualWriterImpl(reader, textualSpec);
	}

	@Override
	protected Class<LinedTextualFileConnectorSpecificConfiguration> getComponentSpecificConfigurationClass(Object reserved)
	{
		return LinedTextualFileConnectorSpecificConfiguration.class;
	}
}

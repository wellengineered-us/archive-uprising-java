/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.streamingio.textual.lined.LinedTextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualSpec;

public class LinedTextualFileConnectorSpecificConfiguration extends
		AbstractTextualFileConnectorSpecificConfiguration<
				LinedTextualFieldSpec,
				LinedTextualSpec<LinedTextualFieldSpec>,
				LinedTextualFieldConfiguration,
				LinedTextualConfiguration>
{
	public LinedTextualFileConnectorSpecificConfiguration()
	{
	}
}

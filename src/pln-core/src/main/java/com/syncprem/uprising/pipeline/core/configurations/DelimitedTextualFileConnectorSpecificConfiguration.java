/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.streamingio.textual.delimited.DelimitedTextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.delimited.DelimitedTextualSpec;

public class DelimitedTextualFileConnectorSpecificConfiguration
		extends AbstractTextualFileConnectorSpecificConfiguration<
		DelimitedTextualFieldSpec,
		DelimitedTextualSpec<DelimitedTextualFieldSpec>,
		DelimitedTextualFieldConfiguration,
		DelimitedTextualConfiguration>
{
	public DelimitedTextualFileConnectorSpecificConfiguration()
	{
	}
}

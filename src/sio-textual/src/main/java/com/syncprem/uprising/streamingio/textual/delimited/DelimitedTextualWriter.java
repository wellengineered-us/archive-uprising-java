/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.delimited;

import com.syncprem.uprising.streamingio.textual.TextualWriter;

public interface DelimitedTextualWriter<TTextualFieldSpec extends DelimitedTextualFieldSpec, TTextualSpec extends DelimitedTextualSpec<TTextualFieldSpec>>
		extends TextualWriter<TTextualFieldSpec, TTextualSpec>
{
}

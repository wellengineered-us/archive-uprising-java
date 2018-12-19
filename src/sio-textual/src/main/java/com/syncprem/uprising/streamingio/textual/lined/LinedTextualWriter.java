/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.lined;

import com.syncprem.uprising.streamingio.textual.TextualWriter;

public interface LinedTextualWriter<TTextualFieldSpec extends LinedTextualFieldSpec, TTextualSpec extends LinedTextualSpec<TTextualFieldSpec>>
		extends TextualWriter<TTextualFieldSpec, TTextualSpec>
{
}

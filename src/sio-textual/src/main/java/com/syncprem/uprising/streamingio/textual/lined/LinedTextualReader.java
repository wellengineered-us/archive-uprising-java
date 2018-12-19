/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.lined;

import com.syncprem.uprising.streamingio.textual.TextualReader;

public interface LinedTextualReader<TTextualFieldSpec extends LinedTextualFieldSpec, TTextualSpec extends LinedTextualSpec<TTextualFieldSpec>>
		extends TextualReader<TTextualFieldSpec, TTextualSpec>
{
}

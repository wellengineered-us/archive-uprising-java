/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.delimited;

import com.syncprem.uprising.streamingio.textual.TextualReader;

public interface DelimitedTextualReader<TTextualFieldSpec extends DelimitedTextualFieldSpec, TTextualSpec extends DelimitedTextualSpec<TTextualFieldSpec>>
		extends TextualReader<TTextualFieldSpec, TTextualSpec>
{
}

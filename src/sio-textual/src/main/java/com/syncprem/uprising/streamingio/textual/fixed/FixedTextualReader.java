/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.streamingio.textual.TextualReader;

public interface FixedTextualReader<TTextualFieldSpec extends FixedTextualFieldSpec, TTextualSpec extends FixedTextualSpec<TTextualFieldSpec>>
		extends TextualReader<TTextualFieldSpec, TTextualSpec>
{
}

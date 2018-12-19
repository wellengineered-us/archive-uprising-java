/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.streamingio.textual.TextualWriter;

public interface FixedTextualWriter<TTextualFieldSpec extends FixedTextualFieldSpec, TTextualSpec extends FixedTextualSpec<TTextualFieldSpec>>
		extends TextualWriter<TTextualFieldSpec, TTextualSpec>
{
}

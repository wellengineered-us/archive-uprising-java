/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.streamingio.textual.TextualFieldSpec;

public interface FixedTextualFieldSpec extends TextualFieldSpec
{
	long getFieldLength();

	long getStartPosition();
}

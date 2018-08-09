/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.streamingio.textual.TextualSpec;

public interface FixedTextualSpec<TTextualFieldSpec extends FixedTextualFieldSpec> extends TextualSpec<TTextualFieldSpec>
{
	char getFillCharacter();

	long getRecordLength();

	boolean usingRecordDelimiter();
}

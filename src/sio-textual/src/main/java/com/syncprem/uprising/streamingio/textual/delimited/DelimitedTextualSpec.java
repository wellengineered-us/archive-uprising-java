/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.delimited;

import com.syncprem.uprising.streamingio.textual.TextualSpec;

public interface DelimitedTextualSpec<TTextualFieldSpec extends DelimitedTextualFieldSpec> extends TextualSpec<TTextualFieldSpec>
{
	String getCloseQuoteValue();

	String getFieldDelimiter();

	String getOpenQuoteValue();
}

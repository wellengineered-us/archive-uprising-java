/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

public interface TextualSpec<TTextualFieldSpec extends TextualFieldSpec>
{
	String getRecordDelimiter();

	Iterable<? extends TTextualFieldSpec> getTextualFooterSpecs();

	Iterable<? extends TTextualFieldSpec> getTextualHeaderSpecs();

	boolean isFirstRecordHeader();

	boolean isLastRecordFooter();

	void assertValid();
}
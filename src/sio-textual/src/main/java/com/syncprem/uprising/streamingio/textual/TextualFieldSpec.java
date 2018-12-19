/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

public interface TextualFieldSpec
{
	String getFieldFormat();

	long getFieldOrdinal();

	String getFieldTitle();

	void setFieldTitle(String fieldTitle);

	TextualFieldType getFieldType();

	boolean isFieldIdentity();

	boolean isFieldRequired();
}

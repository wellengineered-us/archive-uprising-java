/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

public interface DictionarySpec
{
	String getDictionaryId();

	void setDictionaryId(String dictionaryId);

	long getRecordCount();

	void setRecordCount(long recordCount);

	void setPreloadEnabled(boolean preloadEnabled);

	boolean preloadEnabled();
}

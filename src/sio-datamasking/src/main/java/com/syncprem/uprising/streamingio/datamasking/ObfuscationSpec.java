/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

public interface ObfuscationSpec
{
	Iterable<DictionarySpec> getDictionarySpecs();

	void setDictionarySpecs(Iterable<DictionarySpec> dictionarySpecs);

	HashSpec getHashSpec();

	void setHashSpec(HashSpec hashSpec);

	TableSpec getTableSpec();

	void setTableSpec(TableSpec tableSpec);

	void setDisableEngineCaches(boolean disableEngineCaches);

	void setEnablePassThru(boolean enablePassThru);

	void assertValid();

	boolean disableEngineCaches();

	boolean enablePassThru();
}

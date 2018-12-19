/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.streamingio.textual.AbstractTextualSpec;

public class FixedTextualSpecImpl extends AbstractTextualSpec<FixedTextualFieldSpec> implements FixedTextualSpec<FixedTextualFieldSpec>
{
	public FixedTextualSpecImpl()
	{
	}

	private char fillCharacter;
	private long recordLength;

	@Override
	public char getFillCharacter()
	{
		return this.fillCharacter;
	}

	public void setFillCharacter(char fillCharacter)
	{
		this.fillCharacter = fillCharacter;
	}

	@Override
	public long getRecordLength()
	{
		return this.recordLength;
	}

	public void setRecordLength(Long recordLength)
	{
		this.recordLength = recordLength;
	}

	@Override
	public boolean usingRecordDelimiter()
	{
		return !Utils.isNullOrEmptyString(this.getRecordDelimiter());
	}
}

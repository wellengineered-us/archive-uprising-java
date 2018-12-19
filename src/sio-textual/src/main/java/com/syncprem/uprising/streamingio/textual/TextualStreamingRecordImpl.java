/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual;

import java.util.LinkedHashMap;

public class TextualStreamingRecordImpl extends LinkedHashMap<String, Object> implements TextualStreamingRecord
{
	public TextualStreamingRecordImpl()
	{
	}

	public TextualStreamingRecordImpl(long recordIndex, long lineNumber, long characterNumber)
	{
		this.recordIndex = recordIndex;
		this.lineNumber = lineNumber;
		this.characterNumber = characterNumber;
	}

	private static final long serialVersionUID = -5365630128856068164L;
	private long characterNumber;
	private long lineNumber;
	private long recordIndex;

	@Override
	public long getCharacterNumber()
	{
		return this.characterNumber;
	}

	public void setCharacterNumber(long characterNumber)
	{
		this.characterNumber = characterNumber;
	}

	@Override
	public long getLineNumber()
	{
		return this.lineNumber;
	}

	public void setLineNumber(long lineNumber)
	{
		this.lineNumber = lineNumber;
	}

	@Override
	public long getRecordIndex()
	{
		return this.recordIndex;
	}

	public void setRecordIndex(long recordIndex)
	{
		this.recordIndex = recordIndex;
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.textual.fixed;

import com.syncprem.uprising.streamingio.textual.AbstractTextualFieldSpec;

public class FixedTextualFieldSpecImpl extends AbstractTextualFieldSpec implements FixedTextualFieldSpec
{
	public FixedTextualFieldSpecImpl()
	{
	}

	private long fieldLength;
	private long startPosition;

	@Override
	public long getFieldLength()
	{
		return this.fieldLength;
	}

	public void setFieldLength(long fieldLength)
	{
		this.fieldLength = fieldLength;
	}

	@Override
	public long getStartPosition()
	{
		return this.startPosition;
	}

	public void setStartPosition(long startPosition)
	{
		this.startPosition = startPosition;
	}
}

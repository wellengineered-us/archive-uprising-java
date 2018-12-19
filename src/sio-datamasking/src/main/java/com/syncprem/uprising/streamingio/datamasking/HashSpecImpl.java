/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

public final class HashSpecImpl implements HashSpec
{
	public HashSpecImpl()
	{
	}

	private long multiplier;
	private long seed;

	@Override
	public long getMultiplier()
	{
		return this.multiplier;
	}

	@Override
	public void setMultiplier(long multiplier)
	{
		this.multiplier = multiplier;
	}

	@Override
	public long getSeed()
	{
		return this.seed;
	}

	@Override
	public void setSeed(long seed)
	{
		this.seed = seed;
	}
}

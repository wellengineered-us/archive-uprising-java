/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.datamasking;

public interface HashSpec
{
	long getMultiplier();

	void setMultiplier(long multiplier);

	long getSeed();

	void setSeed(long seed);
}

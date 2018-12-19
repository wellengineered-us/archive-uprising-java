/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

import java.util.LinkedHashMap;

public class PartitionImpl extends LinkedHashMap<String, Object> implements Partition
{
	public PartitionImpl()
	{
	}

	public static final PartitionImpl NONE = new PartitionImpl();
	private static final long serialVersionUID = -5365630128856068164L;
}

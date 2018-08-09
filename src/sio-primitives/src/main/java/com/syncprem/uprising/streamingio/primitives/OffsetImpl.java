/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

import java.util.LinkedHashMap;

public class OffsetImpl extends LinkedHashMap<String, Object> implements Offset
{
	public OffsetImpl()
	{
	}

	public static final OffsetImpl NONE = new OffsetImpl();
	private static final long serialVersionUID = -5365630128856068164L;
}

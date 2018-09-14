/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

import com.syncprem.uprising.infrastructure.polyfills.Utils;

import java.util.LinkedHashMap;

public class PayloadImpl extends LinkedHashMap<String, Object> implements Payload
{
	public PayloadImpl()
	{
	}

	public PayloadImpl(int fieldCount)
	{
		super(fieldCount);
	}

	public static final PayloadImpl EMPTY = new PayloadImpl(0);
	private static final long serialVersionUID = -5365630128856068164L;

	public static PayloadImpl fromPrimitive(Object value)
	{
		PayloadImpl payload;

		payload = new PayloadImpl(1);
		payload.put(Utils.EMPTY_STRING, value);

		return payload;
	}
}

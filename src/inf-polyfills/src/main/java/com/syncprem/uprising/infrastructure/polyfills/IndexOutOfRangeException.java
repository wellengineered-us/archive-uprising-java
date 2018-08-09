/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class IndexOutOfRangeException extends IndexOutOfBoundsException
{
	public IndexOutOfRangeException()
	{
		super();
	}

	public IndexOutOfRangeException(String message)
	{
		super(message);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class ObjectDisposedException extends IllegalStateException
{
	public ObjectDisposedException()
	{
		super();
	}

	public ObjectDisposedException(String message)
	{
		super(message);
	}

	public ObjectDisposedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ObjectDisposedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

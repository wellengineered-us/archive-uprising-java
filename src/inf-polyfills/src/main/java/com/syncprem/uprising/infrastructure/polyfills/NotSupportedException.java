/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class NotSupportedException extends IllegalStateException
{
	public NotSupportedException()
	{
		super();
	}

	public NotSupportedException(String message)
	{
		super(message);
	}

	public NotSupportedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotSupportedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

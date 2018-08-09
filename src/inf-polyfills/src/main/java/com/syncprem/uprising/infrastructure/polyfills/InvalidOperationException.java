/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class InvalidOperationException extends IllegalStateException
{
	public InvalidOperationException()
	{
		super();
	}

	public InvalidOperationException(String message)
	{
		super(message);
	}

	public InvalidOperationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidOperationException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

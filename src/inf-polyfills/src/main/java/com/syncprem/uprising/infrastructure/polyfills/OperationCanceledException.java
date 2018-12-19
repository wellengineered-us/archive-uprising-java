/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class OperationCanceledException extends RuntimeException
{
	public OperationCanceledException()
	{
		super();
	}

	public OperationCanceledException(String message)
	{
		super(message);
	}

	public OperationCanceledException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public OperationCanceledException(Throwable cause)
	{
		super(cause);
	}

	protected OperationCanceledException(String message, Throwable cause,
										 boolean enableSuppression,
										 boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

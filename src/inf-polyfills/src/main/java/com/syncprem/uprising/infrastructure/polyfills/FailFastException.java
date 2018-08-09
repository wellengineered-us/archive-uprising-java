/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class FailFastException extends RuntimeException
{
	public FailFastException()
	{
		super();
	}

	public FailFastException(String message)
	{
		super(message);
	}

	public FailFastException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailFastException(Throwable cause)
	{
		super(cause);
	}

	protected FailFastException(String message, Throwable cause,
								boolean enableSuppression,
								boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

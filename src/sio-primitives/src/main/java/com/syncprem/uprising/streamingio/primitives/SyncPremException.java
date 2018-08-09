/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

public class SyncPremException extends Exception
{
	public SyncPremException()
	{
		super();
	}

	public SyncPremException(String message)
	{
		super(message);
	}

	public SyncPremException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SyncPremException(Throwable cause)
	{
		super(cause);
	}

	protected SyncPremException(String message, Throwable cause,
								boolean enableSuppression,
								boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

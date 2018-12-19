/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class ArgumentOutOfRangeException extends IllegalArgumentException
{
	public ArgumentOutOfRangeException()
	{
		super();
	}

	public ArgumentOutOfRangeException(String message)
	{
		super(message);
	}

	public ArgumentOutOfRangeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ArgumentOutOfRangeException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

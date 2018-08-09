/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public class ArgumentNullException extends IllegalArgumentException
{
	public ArgumentNullException()
	{
		super();
	}

	public ArgumentNullException(String message)
	{
		super(message);
	}

	public ArgumentNullException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ArgumentNullException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}

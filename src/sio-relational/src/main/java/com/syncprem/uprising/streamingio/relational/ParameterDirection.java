/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.relational;

public enum ParameterDirection
{
	UNKNOWN(0),
	IN(1),
	OUT(2),
	IN_OUT(1 | 2);

	ParameterDirection(final Integer value)
	{
		this.value = value;
	}

	private Integer value;
}

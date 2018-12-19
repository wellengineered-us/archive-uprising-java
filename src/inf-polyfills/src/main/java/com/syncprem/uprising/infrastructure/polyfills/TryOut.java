/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public final class TryOut<T>
{
	public TryOut()
	{
		this.value = null;
		this.isSet = false;
	}

	public TryOut(T value)
	{
		this.value = value;
		this.isSet = true;
	}

	private boolean isSet;
	private T value;

	public T getValue()
	{
		return this.value;
	}

	public void setValue(T value)
	{
		this.value = value;
		this.setSet(true);
	}

	public boolean isSet()
	{
		return this.isSet;
	}

	private void setSet(boolean isSet)
	{
		this.isSet = isSet;
	}
}

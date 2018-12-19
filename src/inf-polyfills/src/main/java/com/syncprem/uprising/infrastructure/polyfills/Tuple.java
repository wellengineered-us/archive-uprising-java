/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public final class Tuple
{
	public static class Tuple0
	{
		public Tuple0()
		{
		}
	}

	public static class Tuple1<TValue1> extends Tuple0
	{
		public Tuple1(TValue1 value1)
		{
			this.value1 = value1;
		}

		private final TValue1 value1;

		public TValue1 getValue1()
		{
			return this.value1;
		}
	}

	public static class Tuple2<TValue1, TValue2> extends Tuple1<TValue1>
	{
		public Tuple2(TValue1 value1, TValue2 value2)
		{
			super(value1);

			this.value2 = value2;
		}

		private final TValue2 value2;

		public TValue2 getValue2()
		{
			return this.value2;
		}
	}
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.infrastructure.polyfills;

public interface ProcessingCallback
{
	void onProgress(long punctuateModulo, String sourceLabel, long itemIndex, boolean isCompleted, double rollingTiming);
}

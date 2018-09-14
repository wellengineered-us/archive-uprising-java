/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions.runtime;

import com.syncprem.uprising.pipeline.abstractions.Component;

import java.util.Map;

public interface Context extends Component, ChannelFactory, StreamFactory, RecordFactory
{
	Map<String, Object> getGlobalState();

	Map<Component, Map<String, Object>> getLocalState();
}

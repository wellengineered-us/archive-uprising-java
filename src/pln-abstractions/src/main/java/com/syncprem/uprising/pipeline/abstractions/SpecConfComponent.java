/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions;

import com.syncprem.uprising.infrastructure.configuration.Configurable;
import com.syncprem.uprising.pipeline.abstractions.configuration.ComponentSpecificConfiguration;
import com.syncprem.uprising.pipeline.abstractions.configuration.UntypedComponentConfiguration;

public interface SpecConfComponent<TComponentSpecificConfiguration extends ComponentSpecificConfiguration> extends Component, Configurable<UntypedComponentConfiguration>, Specifiable<TComponentSpecificConfiguration>
{
}

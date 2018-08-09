/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions;

import com.syncprem.uprising.pipeline.abstractions.configuration.StageSpecificConfiguration;

public interface Specifiable<TSpecification extends StageSpecificConfiguration>
{
	TSpecification getSpecification();

	void setSpecification(TSpecification specification);

	Class<? extends TSpecification> getSpecificationClass();
}

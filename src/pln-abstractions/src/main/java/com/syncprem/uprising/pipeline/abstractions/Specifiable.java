/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.abstractions;

public interface Specifiable<TSpecification>
{
	TSpecification getSpecification();

	void setSpecification(TSpecification specification);

	Class<? extends TSpecification> getSpecificationClass();
}

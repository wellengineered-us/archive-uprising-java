/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.streamingio.proxywrappers.WrappedIteratorExtensions;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingDataReader;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingParameter;
import com.syncprem.uprising.streamingio.relational.JdbcStreamingResult;
import com.syncprem.uprising.streamingio.relational.uow.UnitOfWorkExtensions;

import java.util.Iterator;

public final class JdbcInsertDestinationConnector extends AbstractJdbcDestinationConnector
{
	public JdbcInsertDestinationConnector()
	{
	}

	@Override
	protected void consumeInternal(Context context, RecordConfiguration configuration, JdbcStreamingDataReader dataReader) throws Exception
	{
		Iterator<JdbcStreamingResult> results;

		Iterable<JdbcStreamingParameter> parameters;

		long rowsCopied = 0;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (dataReader == null)
			throw new ArgumentNullException("dataReader");

		// execute
		if (this.getSpecification().getExecuteCommand() != null &&
				!Utils.isNullOrEmptyString(this.getSpecification().getExecuteCommand().getCommandText()))
		{
			do
			{
				while (dataReader.readRecord())
				{
					parameters = this.getSpecification().getExecuteCommand().getParameters(this.getDestinationUnitOfWork());

					for (JdbcStreamingParameter parameter : parameters)
					{
						if (parameter == null)
							continue;

						// map from source to destination by way of source column on parameter
						final Object value = dataReader.getValue(parameter.getSourceColumn());

						parameter.setValue(value);
					}

					results = UnitOfWorkExtensions.executeResults(this.getDestinationUnitOfWork(),
							this.getSpecification().getExecuteCommand().getCommandType(),
							this.getSpecification().getExecuteCommand().getCommandText(), parameters);

					WrappedIteratorExtensions.forceIteration(results, (index, item) -> WrappedIteratorExtensions.forceIteration(item.getRecords())); // force execution

					rowsCopied++;
				}
			}
			while (dataReader.nextResult());
		}

		System.out.println(String.format("Rows copied = %s.", rowsCopied));
	}
}

/*
	Copyright ©2017-2019 SyncPrem, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.connectors;

import com.syncprem.uprising.infrastructure.polyfills.ArgumentNullException;
import com.syncprem.uprising.infrastructure.polyfills.DelayedProjectionIterator;
import com.syncprem.uprising.infrastructure.polyfills.LifecycleIterator;
import com.syncprem.uprising.infrastructure.polyfills.Utils;
import com.syncprem.uprising.pipeline.abstractions.configuration.RecordConfiguration;
import com.syncprem.uprising.pipeline.abstractions.runtime.Channel;
import com.syncprem.uprising.pipeline.abstractions.runtime.Context;
import com.syncprem.uprising.pipeline.abstractions.runtime.Record;
import com.syncprem.uprising.pipeline.abstractions.stage.connector.destination.AbstractDestinationConnector;
import com.syncprem.uprising.pipeline.core.configurations.AbstractTextualConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.AbstractTextualFieldConfiguration;
import com.syncprem.uprising.pipeline.core.configurations.AbstractTextualFileConnectorSpecificConfiguration;
import com.syncprem.uprising.streamingio.primitives.Payload;
import com.syncprem.uprising.streamingio.textual.TextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.TextualSpec;
import com.syncprem.uprising.streamingio.textual.TextualWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.UUID;

import static com.syncprem.uprising.infrastructure.polyfills.Utils.failFastOnlyWhen;

public abstract class AbstractTextualDestinationConnector
		<
				TTextualFieldConfiguration extends AbstractTextualFieldConfiguration,
				TTextualConfiguration extends AbstractTextualConfiguration<TTextualFieldConfiguration, TTextualFieldSpec, TTextualSpec>,
				TTextualFieldSpec extends TextualFieldSpec,
				TTextualSpec extends TextualSpec<TTextualFieldSpec>,
				TTextualConnectorSpecificConfiguration extends AbstractTextualFileConnectorSpecificConfiguration<TTextualFieldSpec, TTextualSpec, TTextualFieldConfiguration, TTextualConfiguration>,
				TTextualWriter extends TextualWriter<TTextualFieldSpec, TTextualSpec>
				>
		extends AbstractDestinationConnector<TTextualConnectorSpecificConfiguration>
{
	protected AbstractTextualDestinationConnector()
	{
	}

	private TTextualWriter textualWriter;

	public TTextualWriter getTextualWriter()
	{
		return this.textualWriter;
	}

	private void setTextualWriter(TTextualWriter textualWriter)
	{
		this.textualWriter = textualWriter;
	}

	@Override
	protected void consumeInternal(Context context, RecordConfiguration configuration, Channel channel) throws Exception
	{
		Iterator<Record> records;
		LifecycleIterator<Payload> payloads;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (channel == null)
			throw new ArgumentNullException("channel");

		records = channel.getRecords();

		failFastOnlyWhen(records == null, "records	 == null");

		payloads = new DelayedProjectionIterator<>(records, (i, r) -> r.getPayload());

		failFastOnlyWhen(payloads == null, "payloads == null");

		this.getTextualWriter().writeRecords(payloads);
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		super.create(creating);

		if (creating)
		{
			// do nothing
		}
	}

	protected abstract TTextualWriter createTextualWriter(Writer writer, TTextualSpec textualSpec);

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			if (this.getTextualWriter() != null)
			{
				this.getTextualWriter().dispose();
				this.setTextualWriter(null);
			}
		}

		super.dispose(disposing);
	}

	@Override
	protected void postExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		if (this.getTextualWriter() != null)
		{
			this.getTextualWriter().flush();
			this.getTextualWriter().dispose();
			this.setTextualWriter(null);
		}
	}

	@Override
	protected void preExecuteInternal(Context context, RecordConfiguration configuration) throws Exception
	{
		TTextualSpec spec;
		String filePath;

		TTextualConfiguration textualConfiguration;
		TTextualWriter textualWriter;

		if (context == null)
			throw new ArgumentNullException("context");

		if (configuration == null)
			throw new ArgumentNullException("configuration");

		textualConfiguration = this.getSpecification().getTextualConfiguration();

		failFastOnlyWhen(textualConfiguration == null, "textualConfiguration == null");

		spec = textualConfiguration.mapToSpec();

		failFastOnlyWhen(spec == null, "spec == null");

		final File file = new File(this.getSpecification().getTextualFilePath());
		if (file.exists())
			if (file.isDirectory())
				filePath = new File(file.getPath(), UUID.randomUUID().toString()).getAbsolutePath();
			else
				filePath = Utils.failFastOnlyWhen(true, "file.exists() && !file.isDirectory()");
		else
			filePath = file.getAbsolutePath();

		textualWriter = this.createTextualWriter(new FileWriter(filePath), spec);

		failFastOnlyWhen(textualWriter == null, "textualWriter == null");

		this.setTextualWriter(textualWriter);
	}
}

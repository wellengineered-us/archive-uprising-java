/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.pipeline.core.configurations;

import com.syncprem.uprising.infrastructure.polyfills.Message;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualFieldSpec;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualFieldSpecImpl;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualSpec;
import com.syncprem.uprising.streamingio.textual.lined.LinedTextualSpecImpl;

import java.util.Collections;
import java.util.List;

public class LinedTextualConfiguration extends AbstractTextualConfiguration<LinedTextualFieldConfiguration, LinedTextualFieldSpec, LinedTextualSpec<LinedTextualFieldSpec>>
{
	public LinedTextualConfiguration()
	{
	}

	@Override
	public LinedTextualSpec<LinedTextualFieldSpec> mapToSpec()
	{
		LinedTextualSpecImpl spec;

		spec = new LinedTextualSpecImpl();
		spec.setFirstRecordHeader(this.isFirstRecordHeader() == null ? false : this.isFirstRecordHeader());
		spec.setLastRecordHeader(this.isLastRecordFooter() == null ? false : this.isLastRecordFooter());
		spec.setRecordDelimiter(this.getRecordDelimiter());

		if (this.getTextualHeaderConfigs() != null)
		{
			for (LinedTextualFieldConfiguration thisConfig : this.getTextualHeaderConfigs())
			{
				LinedTextualFieldSpecImpl fspec;

				if (thisConfig == null)
					continue;

				fspec = new LinedTextualFieldSpecImpl();
				fspec.setFieldFormat(thisConfig.getFieldFormat());
				fspec.setFieldIdentity(thisConfig.isFieldIdentity() == null ? false : thisConfig.isFieldIdentity());
				fspec.setFieldRequired(thisConfig.isFieldRequired() == null ? false : thisConfig.isFieldRequired());
				fspec.setFieldOrdinal(thisConfig.getFieldOrdinal());
				fspec.setFieldTitle(thisConfig.getFieldTitle());
				fspec.setFieldType(thisConfig.getFieldType());

				spec.getTextualHeaderSpecs().add(fspec);
			}
		}

		return spec;
	}

	@Override
	public Iterable<Message> validate(Object context)
	{
		List<Message> messages = Collections.emptyList();
		return messages;
	}
}

/*
	Copyright ©2017-2018 SyncPrem
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package com.syncprem.uprising.streamingio.primitives;

public interface SchemaBuilder
{
	SchemaBuilder addField(String fieldName, Class<?> fieldClass, boolean isFieldOptional, boolean isKeyComponent, Schema fieldSchema);

	SchemaBuilder addFields(Iterable<Field> fields);

	Schema build();

	SchemaBuilder withName(String value);

	SchemaBuilder withType(SchemaType schemaType);

	SchemaBuilder withVersion(int value);
}

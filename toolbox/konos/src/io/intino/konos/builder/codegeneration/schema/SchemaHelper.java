package io.intino.konos.builder.codegeneration.schema;

import io.intino.konos.dsl.Schema;
import io.intino.konos.dsl.Service;

public class SchemaHelper {

	public static String subPackage(Schema schema) {
		return subPackage(schema.core$().ownerAs(Service.class));
	}

	public static String subPackage(Service service) {
		return "schemas" + (service != null ? java.io.File.separator + service.name$().toLowerCase() : "");
	}

}

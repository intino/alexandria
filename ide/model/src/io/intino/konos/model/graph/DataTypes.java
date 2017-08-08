package io.intino.konos.model.graph;

import io.intino.konos.model.graph.object.ObjectData;

public class DataTypes {
	public static String schemaName(ObjectData self) {
		final Schema schema = self.schema();
		String name = "";
		if (isInService(schema))
			name += schema.core$().owner().name().toLowerCase() + ".";
		return name + firstUpperCase(schema.name$());
	}

	private static boolean isInService(Schema schema) {
		return schema.core$().ownerAs(Service.class) != null;
	}


	private static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public static boolean isComponent(ObjectData self) {
		return isInService(self.schema());
	}
}

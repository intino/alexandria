package io.intino.konos.model.graph;

import io.intino.konos.model.graph.object.ObjectData;
import io.intino.tara.magritte.Node;

public class DataTypes {
	public static String schemaName(ObjectData self) {
		final Schema schema = self.schema();
		String name = "";
		if (isInService(schema))
			name += schema.core$().owner().name().toLowerCase() + ".";
		return name + fullName(self);
	}

	public static String fullName(ObjectData self) {
		final Schema schema = self.schema();
		StringBuilder fullName = new StringBuilder();
		Node node = schema.core$();
		while (node.is(Schema.class)) {
			fullName.append(".").append(firstUpperCase(node.name()));
			node = node.owner();
		}
		return fullName.substring(1);
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

package io.intino.konos.model;

import io.intino.konos.model.object.ObjectData;

public class DataTypes {
	public static String schemaName(ObjectData self) {
		return firstUpperCase(self.schema().name());
	}


	private static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}

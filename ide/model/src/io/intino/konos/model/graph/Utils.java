package io.intino.konos.model.graph;

import java.util.UUID;

public class Utils {


	public static boolean isUUID(String value) {
		try {
			UUID.fromString(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}

package io.intino.konos.dsl;

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

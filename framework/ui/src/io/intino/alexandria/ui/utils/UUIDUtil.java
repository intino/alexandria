package io.intino.alexandria.ui.utils;

import java.util.UUID;
import java.util.stream.IntStream;

public class UUIDUtil {

	public static boolean isUUID(String str) {
		try {
			if (!IntStream.of(new int[]{8, 13, 18, 23}).allMatch((n) -> {
				return str.charAt(n) == '-';
			})) {
				return false;
			} else {
				UUID.fromString(str);
				return true;
			}
		} catch (Exception var2) {
			return false;
		}
	}

}

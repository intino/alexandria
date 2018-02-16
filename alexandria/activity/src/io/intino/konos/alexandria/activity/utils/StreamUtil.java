package io.intino.konos.alexandria.activity.utils;

import java.io.InputStream;

public class StreamUtil {
	public static void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception var2) {
				var2.printStackTrace();
			}
		}
	}
}

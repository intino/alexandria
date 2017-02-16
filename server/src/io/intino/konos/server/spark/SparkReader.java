package io.intino.konos.server.spark;

import io.intino.konos.Error;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import static io.intino.konos.server.spark.RequestAdapter.adapt;

@SuppressWarnings("unchecked")
class SparkReader {

	static <T> T read(String object, Class<T> type) {
		if (type.isAssignableFrom(Error.class) || type.isAssignableFrom(Collection.class))
			return RequestAdapter.adaptFromJSON(object, type);
		else if (type.isAssignableFrom(byte[].class)) return (T) readBytes(object);
		return adapt(object, type);
	}

	static <T> T read(Object object, Class<T> type) {
		return type.isAssignableFrom(InputStream.class) ? (T) object : null;
	}

	private static byte[] readBytes(String object) {
		return object.getBytes(Charset.forName("UTF-8"));
	}


}

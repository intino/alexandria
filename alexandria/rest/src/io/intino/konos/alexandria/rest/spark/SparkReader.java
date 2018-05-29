package io.intino.konos.alexandria.rest.spark;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;

import static io.intino.konos.alexandria.rest.spark.RequestAdapter.adapt;

@SuppressWarnings("unchecked")
class SparkReader {

	static <T> T read(String object, Type type) {
		Class<T> rawType =(Class<T>) ((ParameterizedType) type).getRawType();
		if(Collection.class.isAssignableFrom(rawType)) return readList(object, type);
		return read(object, (Class<T>) rawType);
	}

	private static <T> T readList(String object, Type type) {
		return RequestAdapter.adaptFromJSON(object, type);
	}

	static <T> T read(String object, Class<T> type) {
		if (type.isAssignableFrom(Error.class) || Collection.class.isAssignableFrom(type))
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

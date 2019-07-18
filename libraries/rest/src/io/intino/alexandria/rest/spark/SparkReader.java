package io.intino.alexandria.rest.spark;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;

@SuppressWarnings("unchecked")
public class SparkReader {

	public static <T> T read(String object, Type type) {
		Class<T> rawType =(Class<T>) ((ParameterizedType) type).getRawType();
		if(Collection.class.isAssignableFrom(rawType)) return readList(object, type);
		return read(object, (Class<T>) rawType);
	}

	public static <T> T read(String object, Class<T> type) {
		if (type.isAssignableFrom(Error.class) || Collection.class.isAssignableFrom(type))
			return RequestAdapter.adaptFromJSON(object, type);
		else if (type.isAssignableFrom(byte[].class)) return (T) readBytes(object);
		return RequestAdapter.adapt(object, type);
	}

	public static <T> T read(Object object, Class<T> type) {
		return type.isAssignableFrom(InputStream.class) ? (T) object : null;
	}

	private static <T> T readList(String object, Type type) {
		return RequestAdapter.adaptFromJSON(object, type);
	}

	private static byte[] readBytes(String object) {
		return object.getBytes(Charset.forName("UTF-8"));
	}
}

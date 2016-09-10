package teseo.framework.web;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

@SuppressWarnings("unchecked")
class SparkReader {

	static <T> T read(String object, Class<T> type) {
		if (type == teseo.Error.class || type == Collection.class) return adaptFromJSON(object, type);
		else if (type == File.class) return (T) createTempFile(object);
		else if (type == InputStream.class) return null;
		else if (type == byte[].class) readBytes(object);
		return adapt(object, type);
	}

	private static File createTempFile(String object) {
//		InputStream stream = new ByteArrayInputStream(object.getBytes(StandardCharsets.UTF_8));
		try {
			return Files.write(Files.createTempFile("spark", "input"), object.getBytes()).toFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] readBytes(String object) {
		return object.getBytes(Charset.forName("UTF-8"));
	}

	private static <T> T adapt(String object, Class<T> type) {
		T result = adaptPrimitive(object, type);
		return result != null ? result : adaptFromJSON(object, type);
	}

	private static <T> T adaptFromJSON(String object, Class<T> type) {
		return new Gson().fromJson(object, type);
	}

	private static <T> T adaptPrimitive(String object, Class<T> type) {
		if (type == String.class) return (T) object;
		if (type == Double.class) return (T) Double.valueOf(object);
		if (type == Integer.class) return (T) Integer.valueOf(object);
		if (type == Boolean.class) return (T) Boolean.valueOf(object);
		if (type == LocalDate.class) return (T) LocalDate.ofEpochDay(Long.valueOf(object));
		if (type == LocalDateTime.class) return (T) ofInstant(ofEpochMilli(Long.valueOf(object)), ZoneId.of("UTC"));
		return null;
	}
}

package teseo.framework.utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class Resource {

	public static InputStream getAsStream(String name) {
		return Resource.class.getResourceAsStream(name);
	}

	public static boolean exists(String name) {
		return Resource.class.getResource(name) != null;
	}

	public static String getFullPath(String name) {
		String directory = Resource.class.getResource(name).getPath();

		if (directory.charAt(directory.length() - 1) == '/')
			return directory.substring(0, directory.length() - 1);

		return directory;
	}

	public static Routing toRoute(URL baseRoute, URL resource) {
		return new Routing() {
			private String result = baseRoute.toString() + "/" + encode(resource.toString());

			@Override
			public Routing setContentType(String contentType) {
				if (contentType == null) return this;
				result += concatEncoded(result, "contentType", contentType);
				return this;
			}

			@Override
			public Routing setLabel(String label) {
				if (label == null) return this;
				result += concat(result, "label", label);
				return this;
			}

			@Override
			public URL toUrl() {
				try {
					return new URL(result);
				} catch (MalformedURLException e) {
					return null;
				}
			}
		};
	}

	public interface Routing {
		Routing setContentType(String contentType);
		Routing setLabel(String label);
		URL toUrl();
	}

	private static String concatEncoded(String url, String name, String value) {
		return concat(url, name, encode(value));
	}

	private static String concat(String url, String name, String value) {
		return url.indexOf("?")!=-1?"&":"?" + name + "=" + value;
	}

	private static String encode(String content) {
		return new String(Base64.getEncoder().encode(content.getBytes()));
	}
}

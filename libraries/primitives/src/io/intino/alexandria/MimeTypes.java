package io.intino.alexandria;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class MimeTypes {
	private static Map<String, String> contentTypes = new LinkedHashMap<>();
	private static Map<String, String> extensions = new LinkedHashMap<>();

	public static String contentTypeOf(URL url) {
		try {
			return contentTypeOf(url.toURI());
		} catch (URISyntaxException e) {
			return "";
		}
	}

	public static String contentTypeOf(URI uri) {
		String uriString = uri.toString();
		int pos = uriString.lastIndexOf(".");
		return pos != -1 ? contentTypeOf(uriString.substring(pos)) : null;
	}

	public static String contentTypeOf(String extension) {
		if (!extension.startsWith(".")) extension = "." + extension;
		return contentTypes.getOrDefault(extension, "");
	}

	public static String extensionOf(URL url) {
		return extensionOf(contentTypeOf(url));
	}

	public static String extensionOf(String contentType) {
		return extensions.getOrDefault(contentType, ".bin");
	}

	static {
		contentTypes.put(".aac", "audio/aac");
		contentTypes.put(".abw", "application/x-abiword");
		contentTypes.put(".arc", "application/octet-stream");
		contentTypes.put(".avi", "video/x-msvideo");
		contentTypes.put(".azw", "application/vnd.amazon.ebook");
		contentTypes.put(".bin", "application/octet-stream");
		contentTypes.put(".bz", "application/x-bzip");
		contentTypes.put(".bz2", "application/x-bzip2");
		contentTypes.put(".csh", "application/x-csh");
		contentTypes.put(".css", "text/css");
		contentTypes.put(".csv", "text/csv");
		contentTypes.put(".doc", "application/msword");
		contentTypes.put(".epub", "application/epub+zip");
		contentTypes.put(".gif", "image/gif");
		contentTypes.put(".htm", "text/html");
		contentTypes.put(".html", "text/html");
		contentTypes.put(".ico", "image/x-icon");
		contentTypes.put(".ics", "text/calendar");
		contentTypes.put(".jar", "application/java-archive");
		contentTypes.put(".jpg", "image/jpeg");
		contentTypes.put(".jpeg", "image/jpeg");
		contentTypes.put(".js", "application/javascript");
		contentTypes.put(".json", "application/json");
		contentTypes.put(".mid", "audio/midi");
		contentTypes.put(".midi", "audio/midi");
		contentTypes.put(".mpeg", "video/mpeg");
		contentTypes.put(".mpkg", "application/vnd.apple.installer+xml");
		contentTypes.put(".odp", "application/vnd.oasis.opendocument.presentation");
		contentTypes.put(".ods", "application/vnd.oasis.opendocument.spreadsheet");
		contentTypes.put(".odt", "application/vnd.oasis.opendocument.text");
		contentTypes.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		contentTypes.put(".oga", "audio/ogg");
		contentTypes.put(".ogv", "video/ogg");
		contentTypes.put(".ogx", "application/ogg");
		contentTypes.put(".pdf", "application/pdf");
		contentTypes.put(".ppt", "application/vnd.ms-powerpoint");
		contentTypes.put(".rar", "application/x-rar-compressed");
		contentTypes.put(".rtf", "application/rtf");
		contentTypes.put(".sh", "application/x-sh");
		contentTypes.put(".svg", "image/svg+xml");
		contentTypes.put(".swf", "application/x-shockwave-flash");
		contentTypes.put(".tar", "application/x-tar");
		contentTypes.put(".tiff", "image/tiff");
		contentTypes.put(".ttf", "font/ttf");
		contentTypes.put(".tif", "image/tiff");
		contentTypes.put(".vsd", "application/vnd.visio");
		contentTypes.put(".wav", "audio/x-wav");
		contentTypes.put(".weba", "audio/webm");
		contentTypes.put(".webm", "video/webm");
		contentTypes.put(".webp", "image/webp");
		contentTypes.put(".woff", "font/woff");
		contentTypes.put(".woff2", "font/woff2");
		contentTypes.put(".xhtml", "application/xhtml+xml");
		contentTypes.put(".xls", "application/vnd.ms-excel");
		contentTypes.put(".xml", "application/xml");
		contentTypes.put(".xul", "application/vnd.mozilla.xul+xml");
		contentTypes.put(".zip", "application/zip");
		contentTypes.put(".3gp", "video/3gpp");
		contentTypes.put(".3g2", "video/3gpp2");
		contentTypes.put(".7z", "application/x-7z-compressed");
		registerExtensions();
	}

	private static void registerExtensions() {
		contentTypes.forEach((key, value) -> extensions.put(value, key));
	}

}

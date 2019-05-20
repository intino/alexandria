package io.intino.alexandria.ui.utils;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UrlUtil {

	public static URL toURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			Logger.error(e);
			return null;
		}
	}

	public static String mimeType(URL file) {
		try {
			return Files.probeContentType(Paths.get(file.toURI()));
		} catch (IOException | URISyntaxException e) {
			return "application/octet-stream";
		}
	}

	public static String mimeType(File file) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		return fileNameMap.getContentTypeFor(file.getName());
	}

}

package io.intino.alexandria;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Resource {
	private String name;
	private String fileName;
	private final InputStream content;
	private final String contentType;
	private Map<String, String> parameters;

	public Resource(String name, String fileName, String contentType, InputStream content) {
		this.name = name;
		this.fileName = fileName;
		this.content = content;
		this.contentType = contentType;
		this.parameters = new HashMap<>();
	}

	public static String resolveContentType(File file) throws IOException {
		String type = Files.probeContentType(file.toPath());
		if (type != null) return type;
		type = URLConnection.guessContentTypeFromName(file.getName());
		return type != null ? type : URLConnection.guessContentTypeFromStream(new BufferedInputStream(new FileInputStream(file)));
	}

	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}

	public InputStream content() {
		return content;
	}

	public String name() {
		return name;
	}

	public String fileName() {
		return fileName;
	}

	public String contentType() {
		return contentType;
	}

	public Map<String, String> parameters() {
		return parameters;
	}
}

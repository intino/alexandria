package io.intino.alexandria;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;

public class Resource {
	private String id;
	private String type;
	private String contentType;
	private InputStream data;

	public Resource(String id) {
		this.id = id;
		this.type = id.substring(id.lastIndexOf('.') + 1);
		this.data = null;
	}

	public static String getMimeType(String name) {
		return URLConnection.getFileNameMap().getContentTypeFor(name);
	}

	public static String getExtension(String contentType) {
		return null; // TODO
	}

	public String id() {
		return id;
	}

	public String type() {
		return type;
	}

	public String contentType() {
		return contentType;
	}

	public InputStream data() {
		return data;
	}

	public String mimeType() {
		return getMimeType(id);
	}

	public Resource contentType(String type) {
		contentType = type;
		return this;
	}

	public Resource data(byte[] data) {
		this.data = new ByteArrayInputStream(data);
		return this;
	}

	public Resource data(InputStream stream) {
		this.data = stream;
		return this;
	}

	@Override
	public String toString() {
		return id;
	}

	public static String resolveContentType(File file) throws IOException {
		String type = Files.probeContentType(file.toPath());
		if (type != null) return type;
		type = URLConnection.guessContentTypeFromName(file.getName());
		return type != null ? type : URLConnection.guessContentTypeFromStream(new BufferedInputStream(new FileInputStream(file)));
	}

	public static Resource parse(String text) {
		return new Resource(text.substring(1));
	}
}

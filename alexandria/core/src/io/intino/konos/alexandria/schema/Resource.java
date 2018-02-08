package io.intino.konos.alexandria.schema;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Resource {
	private String id;
	private String type;
	private byte[] data;

	private static Map<String, String> mimeTypes;

	public Resource(String id) {
		this.id = id;
		this.type = id.substring(id.lastIndexOf('.') + 1);
		this.data = new byte[0];
	}

	public String id() {
		return id;
	}

	public String type() {
		return type;
	}

	public String mimeType() {
		return mimeTypes.getOrDefault(type, "application/" + type);
	}

	public byte[] data() {
		return data;
	}

	public Resource data(byte[] data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return id;
	}

	static {
		mimeTypes = new HashMap<>();
		mimeTypes.put("txt", "text/plain");
		mimeTypes.put("pdf", "application/pdf");
		mimeTypes.put("png", "image/png");
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

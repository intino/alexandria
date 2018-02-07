package io.intino.konos.alexandria.schema;

import java.util.HashMap;
import java.util.Map;

public class Resource {
	private String id;
	private String type;
	private byte[] data;

	private static Map<String, String> mimeTypes;

	public String id() {
		return id;
	}

	public String type() {
		return type;
	}

	public String mimeType() {
		return type; //TODO
	}

	public byte[] data() {
		return data;
	}


	static {
		mimeTypes = new HashMap<>();
		mimeTypes.put("txt", "text/plain");
		mimeTypes.put("pdf", "application/pdf");
		mimeTypes.put("png", "image/png");
	}
}

package io.intino.alexandria.cli.response;

import io.intino.alexandria.cli.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Attachment extends Response {
	public Type getType() {
		return type;
	}

	public enum Type {File, Image}

	private final InputStream inputStream;
	private final String title;
	private final String fileName;
	private final Type type;

	public Attachment(InputStream inputStream, String title, String fileName, Type type) {
		this.inputStream = inputStream;
		this.title = title;
		this.fileName = fileName;
		this.type = type;
	}

	public Attachment(byte[] bytes, String title, String fileName, Type type) {
		this(new ByteArrayInputStream(bytes), title, fileName, type);
	}

	public String getTitle() {
		return title;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
}
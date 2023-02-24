package io.intino.alexandria.cli.schemas;

public class BotResponse implements java.io.Serializable {

	private Type type;

	public enum Type {
		text, file, image, question, multiline
	}

	private String raw;
	private String title;
	private String fileName;
	private java.util.List<String> options = new java.util.ArrayList<>();

	public Type type() {
		return type;
	}

	public String raw() {
		return this.raw;
	}

	public String title() {
		return this.title;
	}

	public String fileName() {
		return this.fileName;
	}

	public java.util.List<String> options() {
		return this.options;
	}

	public BotResponse type(Type type) {
		this.type = type;
		return this;
	}

	public BotResponse raw(String raw) {
		this.raw = raw;
		return this;
	}

	public BotResponse title(String title) {
		this.title = title;
		return this;
	}

	public BotResponse fileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public BotResponse options(java.util.List<String> options) {
		this.options = options;
		return this;
	}
}
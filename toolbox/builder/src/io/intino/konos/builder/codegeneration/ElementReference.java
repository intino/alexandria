package io.intino.konos.builder.codegeneration;

public class ElementReference {
	private String name;
	private String type;

	public String name() {
		return name;
	}

	public ElementReference name(String name) {
		this.name = name;
		return this;
	}

	public String type() {
		return type;
	}

	public ElementReference type(String type) {
		this.type = type;
		return this;
	}

	public static ElementReference from(String content) {
		String[] data = content.split("#");
		ElementReference result = new ElementReference().name(data[0]);
		if (data.length > 1) result.type(data[1]);
		return result;
	}

	public static ElementReference of(String name, String type) {
		return new ElementReference().name(name).type(type);
	}

	public String toString() {
		return name + "#" + type;
	}
}

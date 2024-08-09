package io.intino.alexandria.ollama.tools;

import io.intino.alexandria.Json;

public class OllamaTool {

	public static OllamaTool of(OllamaFunction function) {
		return new OllamaTool(Type.function, function);
	}

	private Type type;
	private OllamaFunction function;

	public OllamaTool(Type type, OllamaFunction function) {
		this.type = type;
		this.function = function;
	}

	public Type type() {
		return type;
	}

	public OllamaTool type(Type type) {
		this.type = type;
		return this;
	}

	public OllamaFunction function() {
		return function;
	}

	public OllamaTool function(OllamaFunction function) {
		this.function = function;
		return this;
	}

	@Override
	public String toString() {
		return Json.toJson(this);
	}

	public enum Type {
		function
	}
}

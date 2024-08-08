package io.intino.alexandria.ollama.requests;

import io.intino.alexandria.ollama.ModelFile;

public class OllamaCreateModelRequest extends OllamaRequest {

	private String name;
	private String modelfile;
	private boolean stream;
	private String path;

	public String name() {
		return name;
	}

	public OllamaCreateModelRequest name(String name) {
		this.name = name;
		return this;
	}

	public String modelfile() {
		return modelfile;
	}

	public OllamaCreateModelRequest modelfile(String modelfile) {
		this.modelfile = modelfile;
		return this;
	}

	public OllamaCreateModelRequest modelfile(ModelFile modelfile) {
		this.modelfile = modelfile == null ? null : modelfile.toString();
		return this;
	}

	public boolean stream() {
		return stream;
	}

	public OllamaCreateModelRequest stream(boolean stream) {
		this.stream = stream;
		return this;
	}

	public String path() {
		return path;
	}

	public OllamaCreateModelRequest path(String path) {
		this.path = path;
		return this;
	}
}

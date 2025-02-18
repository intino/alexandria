package io.intino.alexandria.ollama.requests;

public class OllamaShowRequest extends OllamaRequest {

	private String name;
	private boolean verbose;

	public String name() {
		return name;
	}

	public OllamaShowRequest name(String name) {
		this.name = name;
		return this;
	}

	public boolean verbose() {
		return verbose;
	}

	public OllamaShowRequest verbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}
}

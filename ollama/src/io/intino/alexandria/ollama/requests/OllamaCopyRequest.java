package io.intino.alexandria.ollama.requests;

public class OllamaCopyRequest extends OllamaRequest {

	private String source;
	private String destination;

	public String source() {
		return source;
	}

	public OllamaCopyRequest source(String source) {
		this.source = source;
		return this;
	}

	public String destination() {
		return destination;
	}

	public OllamaCopyRequest destination(String destination) {
		this.destination = destination;
		return this;
	}
}

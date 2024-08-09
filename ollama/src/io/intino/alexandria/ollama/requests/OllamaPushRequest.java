package io.intino.alexandria.ollama.requests;

public class OllamaPushRequest extends OllamaRequest {

	private String name;
	private boolean insecure;
	private boolean stream;

	public String name() {
		return name;
	}

	public OllamaPushRequest name(String model) {
		this.name = model;
		return this;
	}

	public boolean insecure() {
		return insecure;
	}

	public OllamaPushRequest insecure(boolean insecure) {
		this.insecure = insecure;
		return this;
	}

	public boolean stream() {
		return stream;
	}

	public OllamaPushRequest stream(boolean stream) {
		this.stream = stream;
		return this;
	}
}

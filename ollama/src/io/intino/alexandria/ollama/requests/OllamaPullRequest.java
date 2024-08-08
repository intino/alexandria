package io.intino.alexandria.ollama.requests;

public class OllamaPullRequest extends OllamaRequest {

	private String name;
	private boolean insecure;
	private boolean stream;

	public String name() {
		return name;
	}

	public OllamaPullRequest name(String model) {
		this.name = model;
		return this;
	}

	public boolean insecure() {
		return insecure;
	}

	public OllamaPullRequest insecure(boolean insecure) {
		this.insecure = insecure;
		return this;
	}

	public boolean stream() {
		return stream;
	}

	public OllamaPullRequest stream(boolean stream) {
		this.stream = stream;
		return this;
	}
}

package io.intino.alexandria.ollama.responses;

public class OllamaCreateModelResponse extends OllamaResponse {

	private String status;

	public String status() {
		return status;
	}

	public OllamaCreateModelResponse status(String status) {
		this.status = status;
		return this;
	}
}

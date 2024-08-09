package io.intino.alexandria.ollama;

public class OllamaAPIException extends Exception {

	private Integer statusCode;

	public OllamaAPIException() {
	}

	public OllamaAPIException(String message) {
		super(message);
	}

	public OllamaAPIException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public OllamaAPIException(String message, Throwable cause) {
		super(message, cause);
	}

	public OllamaAPIException(Throwable cause) {
		super(cause);
	}

	public Integer statusCode() {
		return statusCode;
	}
}

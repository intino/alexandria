package io.intino.konos.alexandria.exceptions;

import java.util.Map;

import static java.util.Collections.emptyMap;

public abstract class AlexandriaException extends Throwable {

	private final Map<String, String> parameters;
	private String code;

	public AlexandriaException(String code, String message) {
		this(code, message, emptyMap());
	}

	public AlexandriaException(String code, String message, Map<String, String> parameters) {
		super(message);
		this.code = code;
		this.parameters = parameters;
	}

	public String code() {
		return code;
	}

	public String message() {
		return getMessage();
	}

	public Map<String, String> parameters() {
		return parameters;
	}
}

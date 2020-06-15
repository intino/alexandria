package io.intino.alexandria.restaccessor.exceptions;

import io.intino.alexandria.exceptions.AlexandriaError;

import java.util.Map;

public class RestfulFailure extends Exception implements AlexandriaError {
	private final String message;
	private final Map<String, String> parameters;
	private String code = "err:rff";

	public RestfulFailure(String message) {
		this.message = message;
		parameters = Map.of();
	}

	public RestfulFailure(String code, String message) {
		this.code = code;
		this.message = message;
		parameters = Map.of();
	}

	public RestfulFailure(String code, String message, Map<String, String> parameters) {
		this.code = code;
		this.message = message;
		this.parameters = parameters;
	}

	@Override
	public String code() {
		return this.code;
	}

	@Override
	public String message() {
		return message;
	}

	@Override
	public Map<String, String> parameters() {
		return parameters;
	}
}

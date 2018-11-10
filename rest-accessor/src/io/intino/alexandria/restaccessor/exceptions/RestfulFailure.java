package io.intino.alexandria.restaccessor.exceptions;

import io.intino.alexandria.exceptions.AlexandriaError;

import java.util.Map;

public class RestfulFailure extends Exception implements AlexandriaError {
	private final String message;
	private String code = "err:rff";

	public RestfulFailure(String message) {
		this.message = message;
	}

	public RestfulFailure(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String code() {
		return this.code;
	}

	@Override
	public String label() {
		return message;
	}

	@Override
	public Map<String, String> parameters() {
		return null;
	}
}

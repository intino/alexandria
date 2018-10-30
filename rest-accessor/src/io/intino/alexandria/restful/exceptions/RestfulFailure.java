package io.intino.alexandria.restful.exceptions;

import io.intino.alexandria.Error;

import java.util.Map;

public class RestfulFailure extends Exception implements Error {
	private String code = "err:rff";
	private final String message;

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

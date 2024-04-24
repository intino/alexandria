package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Rule;

public enum ExceptionCodes implements Rule<Enum> {

	BadRequest("400"),
	Unauthorized("401"),
	Forbidden("403"),
	NotFound("404"),
	Conflict("409"),
	Locked("423"),
	NotImplemented("501"),
	ServiceUnavailable("503");

	private final String code;

	ExceptionCodes(String code) {
		this.code = code;
	}

	public String value() {
		return code;
	}

	@Override
	public boolean accept(Enum value) {
		return value instanceof ExceptionCodes;
	}
}

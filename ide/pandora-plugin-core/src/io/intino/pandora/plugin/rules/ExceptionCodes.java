package io.intino.pandora.plugin.rules;

import tara.lang.model.Rule;

public enum ExceptionCodes implements Rule<Enum> {

	BadRequest("400"),
	Unauthorized("401"),
	Forbidden("403"),
	NotFound("404"),
	Conflict("409");

	private String code;

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

package io.intino.alexandria.exceptions;

import java.util.Map;

@Deprecated
public class Unknown extends InternalServerError {

	public Unknown(String message) {
		super(message);
	}

	public Unknown(String message, Map<String, String> parameters) {
		super(message, parameters);
	}
}

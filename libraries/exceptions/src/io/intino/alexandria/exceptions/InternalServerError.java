package io.intino.alexandria.exceptions;

import java.util.Map;

public class InternalServerError extends AlexandriaException {

	public InternalServerError(String message) {
		super("500", message);
	}

	public InternalServerError(String message, Map<String, String> parameters) {
		super("500", message, parameters);
	}

}

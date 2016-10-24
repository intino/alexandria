package io.intino.pandora.exceptions;

import java.util.Map;

public class BadRequest extends PandoraException {

	public BadRequest(String message) {
		super("400", message);
	}

	public BadRequest(String message, Map<String, String> parameters) {
		super("400", message, parameters);
	}
}

package io.intino.konos.exceptions;

import java.util.Map;

public class BadRequest extends KonosException {

	public BadRequest(String message) {
		super("400", message);
	}

	public BadRequest(String message, Map<String, String> parameters) {
		super("400", message, parameters);
	}
}

package io.intino.pandora.exceptions;

import java.util.Map;

public class NotFound extends PandoraException {

	public NotFound(String message) {
		super("404", message);
	}

	public NotFound(String message, Map<String, String> parameters) {
		super("404", message, parameters);
	}

}

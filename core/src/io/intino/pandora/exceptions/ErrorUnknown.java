package io.intino.pandora.exceptions;

import java.util.Map;

public class ErrorUnknown extends PandoraException {

	public ErrorUnknown(String message) {
		super("500", message);
	}

	public ErrorUnknown(String message, Map<String, String> parameters) {
		super("500", message, parameters);
	}

}

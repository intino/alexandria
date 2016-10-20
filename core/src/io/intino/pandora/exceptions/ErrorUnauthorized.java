package io.intino.pandora.exceptions;


import java.util.Map;

public class ErrorUnauthorized extends PandoraException {

	public ErrorUnauthorized(String message) {
		super("401", message);
	}

	public ErrorUnauthorized(String message, Map<String, String> parameters) {
		super("401", message, parameters);
	}
}

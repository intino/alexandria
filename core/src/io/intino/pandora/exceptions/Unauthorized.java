package io.intino.pandora.exceptions;


import java.util.Map;

public class Unauthorized extends PandoraException {

	public Unauthorized(String message) {
		super("401", message);
	}

	public Unauthorized(String message, Map<String, String> parameters) {
		super("401", message, parameters);
	}
}

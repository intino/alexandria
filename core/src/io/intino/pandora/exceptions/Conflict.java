package io.intino.pandora.exceptions;


import java.util.Map;

public class Conflict extends PandoraException {

	public Conflict(String message) {
		super("409", message);
	}

	public Conflict(String message, Map<String, String> parameters) {
		super("409", message, parameters);
	}
}

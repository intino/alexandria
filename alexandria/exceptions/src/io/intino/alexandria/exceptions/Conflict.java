package io.intino.alexandria.exceptions;


import java.util.Map;

public class Conflict extends AlexandriaException {

	public Conflict(String message) {
		super("409", message);
	}

	public Conflict(String message, Map<String, String> parameters) {
		super("409", message, parameters);
	}
}

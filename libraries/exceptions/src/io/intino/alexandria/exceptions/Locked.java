package io.intino.alexandria.exceptions;


import java.util.Map;

public class Locked extends AlexandriaException {

	public Locked(String message) {
		super("423", message);
	}

	public Locked(String message, Map<String, String> parameters) {
		super("423", message, parameters);
	}
}

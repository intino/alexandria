package io.intino.alexandria.exceptions;


import java.util.Map;

public class Forbidden extends AlexandriaException {

	public Forbidden(String message) {
		super("403", message);
	}

	public Forbidden(String message, Map<String, String> parameters) {
		super("403", message, parameters);
	}
}

package io.intino.alexandria.exceptions;

import java.util.Map;

public class NotImplemented extends AlexandriaException {

	public NotImplemented(String message) {
		super("501", message);
	}

	public NotImplemented(String message, Map<String, String> parameters) {
		super("501", message, parameters);
	}

}

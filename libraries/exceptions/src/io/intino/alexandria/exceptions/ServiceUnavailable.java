package io.intino.alexandria.exceptions;

import java.util.Map;

public class ServiceUnavailable extends AlexandriaException {

	public ServiceUnavailable(String message) {
		super("503", message);
	}

	public ServiceUnavailable(String message, Map<String, String> parameters) {
		super("503", message, parameters);
	}
}
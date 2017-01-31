package io.intino.konos.exceptions;


import java.util.Map;

public class Conflict extends KonosException {

	public Conflict(String message) {
		super("409", message);
	}

	public Conflict(String message, Map<String, String> parameters) {
		super("409", message, parameters);
	}
}

package io.intino.konos.exceptions;


import java.util.Map;

public class Unauthorized extends KonosException {

	public Unauthorized(String message) {
		super("401", message);
	}

	public Unauthorized(String message, Map<String, String> parameters) {
		super("401", message, parameters);
	}
}

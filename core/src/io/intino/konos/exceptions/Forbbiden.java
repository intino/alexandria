package io.intino.konos.exceptions;


import java.util.Map;

public class Forbbiden extends KonosException {

	public Forbbiden(String message) {
		super("403", message);
	}

	public Forbbiden(String message, Map<String, String> parameters) {
		super("403", message, parameters);
	}
}

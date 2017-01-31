package io.intino.konos.exceptions;

import java.util.Map;

public class Unknown extends KonosException {

	public Unknown(String message) {
		super("500", message);
	}

	public Unknown(String message, Map<String, String> parameters) {
		super("500", message, parameters);
	}

}

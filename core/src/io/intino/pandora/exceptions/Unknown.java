package io.intino.pandora.exceptions;

import java.util.Map;

public class Unknown extends PandoraException {

	public Unknown(String message) {
		super("500", message);
	}

	public Unknown(String message, Map<String, String> parameters) {
		super("500", message, parameters);
	}

}

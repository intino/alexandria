package org.siani.pandora.exceptions;

import java.util.Map;

public class ErrorNotFound extends PandoraException {

	public ErrorNotFound(String message) {
		super("404", message);
	}

	public ErrorNotFound(String message, Map<String, String> parameters) {
		super("404", message, parameters);
	}

}

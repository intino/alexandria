package org.siani.pandora.exceptions;


import java.util.Map;

public class ErrorConflict extends PandoraException {

	public ErrorConflict(String message) {
		super("409", message);
	}

	public ErrorConflict(String message, Map<String, String> parameters) {
		super("409", message, parameters);
	}
}

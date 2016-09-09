package teseo.exceptions;


import java.util.Map;

public class ErrorConflict extends TeseoException {

	public ErrorConflict(String message) {
		super("409", message);
	}

	public ErrorConflict(String message, Map<String, String> parameters) {
		super("409", message, parameters);
	}
}

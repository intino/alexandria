package teseo.exceptions;

import java.util.Map;

public class ErrorBadRequest extends TeseoException {

	public ErrorBadRequest(String message) {
		super("400", message);
	}

	public ErrorBadRequest(String message, Map<String, String> parameters) {
		super("400", message, parameters);
	}
}

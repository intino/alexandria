package teseo.exceptions;


import java.util.Map;

public class ErrorForbbiden extends TeseoException {

	public ErrorForbbiden(String message) {
		super("403", message);
	}

	public ErrorForbbiden(String message, Map<String, String> parameters) {
		super("403", message, parameters);
	}
}

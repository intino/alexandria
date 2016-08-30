package teseo.exceptions;

public class ErrorNotFound extends TeseoException {

	public ErrorNotFound(String message) {
		super("404", message);
	}
}

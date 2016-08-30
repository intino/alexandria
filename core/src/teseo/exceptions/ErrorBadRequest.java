package teseo.exceptions;

public class ErrorBadRequest extends TeseoException {

	public ErrorBadRequest(String message) {
		super("400", message);
	}
}

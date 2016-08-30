package teseo.exceptions;

public abstract class TeseoException extends Throwable {

	private String code;

	public TeseoException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String code() {
		return code;
	}

	public String message() {
		return getMessage();
	}

}

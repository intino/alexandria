package teseo.exceptions;

import java.util.Map;

import static java.util.Collections.emptyMap;

public abstract class TeseoException extends Throwable {

	private final Map<String, String> parameters;
	private String code;

	public TeseoException(String code, String message) {
		this(code, message, emptyMap());
	}

	public TeseoException(String code, String message, Map<String, String> parameters) {
		super(message);
		this.code = code;
		this.parameters = parameters;
	}

	public String code() {
		return code;
	}

	public String message() {
		return getMessage();
	}

	public Map<String, String> parameters() {
		return parameters;
	}
}

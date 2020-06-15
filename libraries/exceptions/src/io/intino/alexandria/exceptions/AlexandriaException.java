package io.intino.alexandria.exceptions;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.joining;

public class AlexandriaException extends Throwable implements AlexandriaError {
	private final Map<String, String> parameters;
	private final String code;

	public AlexandriaException(String code, String message) {
		this(code, message, emptyMap());
	}

	public AlexandriaException(String code, String message, Map<String, String> parameters) {
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

	@Override
	public String toString() {
		return "{\n" +
				"\t\"code\": \"" + code() + "\",\n" +
				"\t\"detailMessage\": \"" + message() + "\",\n" +
				"\t\"parameters\": {\n\t\t" +
				parameters().entrySet().stream().map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"").collect(joining(",\n\t\t")) +
				"\n\t}\n" +
				"}";
	}
}

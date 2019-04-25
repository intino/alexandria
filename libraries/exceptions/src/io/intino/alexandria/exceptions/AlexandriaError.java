package io.intino.alexandria.exceptions;

import java.util.Map;

public interface AlexandriaError {
	String code();

	String label();

	Map<String, String> parameters();
}

package io.intino.alexandria;

import java.io.InputStream;

public interface Session {
	String SessionExtension = ".session";

	String name();

	Type type();

	InputStream inputStream();

	enum Type {
		message,
		tuple,
		measurement
	}
}

package io.intino.alexandria.restaccessor;

import java.io.InputStream;
import java.util.Map;

public interface Response {
	int code();

	String content();

	Map<String, String> headers();

	String contentType();

	InputStream contentAsStream();
}

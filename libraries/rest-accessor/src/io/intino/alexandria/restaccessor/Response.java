package io.intino.alexandria.restaccessor;

import java.io.InputStream;

public interface Response {
	int code();

	String content();

	InputStream contentAsStream();
}

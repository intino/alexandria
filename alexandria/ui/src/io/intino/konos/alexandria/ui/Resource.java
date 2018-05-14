package io.intino.konos.alexandria.ui;

import java.io.InputStream;

public interface Resource {
	String label();
	InputStream content();
}

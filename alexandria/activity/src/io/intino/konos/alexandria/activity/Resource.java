package io.intino.konos.alexandria.activity;

import java.io.InputStream;

public interface Resource {
	String label();
	InputStream content();
}

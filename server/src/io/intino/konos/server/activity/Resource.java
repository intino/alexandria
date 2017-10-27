package io.intino.konos.server.activity;

import java.io.InputStream;

public interface Resource {
	String label();
	InputStream content();
}

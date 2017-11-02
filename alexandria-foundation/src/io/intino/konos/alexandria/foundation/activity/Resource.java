package io.intino.konos.alexandria.foundation.activity;

import java.io.InputStream;

public interface Resource {
	String label();
	InputStream content();
}

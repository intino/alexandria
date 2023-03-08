package io.intino.alexandria;

import io.intino.alexandria.event.Event.Format;

import java.io.InputStream;

public interface Session {
	String SessionExtension = ".session";

	String name();

	Format format();

	InputStream inputStream();
}

package io.intino.alexandria;

import java.io.InputStream;

public interface Session {
	String SessionExtension = ".session";
	String EventSessionExtension = ".event.session";
	String LedSessionExtension = ".ledger.session";

	String name();

	Type type();

	InputStream inputStream();

	enum Type {
		event,
		set,
		setMetadata,
		led
	}
}

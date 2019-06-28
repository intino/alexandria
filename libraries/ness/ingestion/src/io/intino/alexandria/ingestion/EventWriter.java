package io.intino.alexandria.ingestion;

import java.io.IOException;

public interface EventWriter {
	void write(String str) throws IOException;

	void flush();

	void close();
}

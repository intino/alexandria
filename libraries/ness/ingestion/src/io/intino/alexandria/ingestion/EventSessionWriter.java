package io.intino.alexandria.ingestion;

public interface EventSessionWriter {

	void write(String text);

	void flush();

	void close();
}

package io.intino.alexandria.ingestion;

import io.intino.alexandria.Timetag;

public interface SetSessionWriter {
	void add(String tank, Timetag timetag, String set, long id);

	void flush();

	void close();
}
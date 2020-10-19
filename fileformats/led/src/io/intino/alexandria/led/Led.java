package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;

public interface Led<S extends Schema> {
	long size();

	int schemaSize();

	Iterator<S> iterator();

	List<S> elements();
}
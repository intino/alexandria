package io.intino.alexandria.mapp;

import java.util.Iterator;

public interface MappStream extends Iterator<MappStream.Item> {

	boolean hasNext();

	Item next();

	void close();

	interface Item {
		long key();

		String value();
	}


}

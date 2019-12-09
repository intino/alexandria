package io.intino.alexandria.tabb;

import java.util.ArrayList;
import java.util.Collection;

public class Row extends ArrayList<Value> {

	public Row(int initialCapacity) {
		super(initialCapacity);
	}

	public Row() {
		super();
	}

	public Row(Collection<? extends Value> c) {
		super(c);
	}
}
package io.intino.alexandria.assa;

import java.io.Serializable;

public interface Assa<T extends Serializable> {
	String name();

	int size();

	T get(long key);
}

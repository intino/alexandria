package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;

public interface Led<S extends Transaction> {
	long size();

	int transactionSize();

	Iterator<S> iterator();

	List<S> elements();
}
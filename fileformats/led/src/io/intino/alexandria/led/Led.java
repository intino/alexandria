package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;

public interface Led<S extends Transaction> {

	long size();

	int transactionSize();

	S transaction(int index);

	Iterator<S> iterator();

	List<S> elements();
}
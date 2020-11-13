package io.intino.alexandria.led;

import io.intino.alexandria.led.leds.IteratorLedStream;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

public interface Led<T extends Transaction> {

	long size();

	int transactionSize();

	T transaction(int index);

	Iterator<T> iterator();

	List<T> elements();

	default LedStream<T> stream() {
		return new IteratorLedStream<>(transactionSize(), iterator());
	}
}
package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Transaction;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListLed<S extends Transaction> implements Led<S> {

	private final List<S> list;

	public ListLed(List<S> list) {
		this.list = Collections.unmodifiableList(list);
	}

	@Override
	public long size() {
		return list.size();
	}

	@Override
	public int transactionSize() {
		return list.isEmpty() ? 0 : list.get(0).size();
	}

	@Override
	public S transaction(int index) {
		return list.get(index);
	}

	@Override
	public Iterator<S> iterator() {
		return list.iterator();
	}

	@Override
	public List<S> elements() {
		return list;
	}
}
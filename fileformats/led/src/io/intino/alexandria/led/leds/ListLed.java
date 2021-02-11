package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListLed<T extends Schema> implements Led<T> {

	private final Class<T> schemaClass;
	private final List<T> list;

	public ListLed(Class<T> schemaClass, List<T> list) {
		this.schemaClass = schemaClass;
		this.list = Collections.unmodifiableList(list);
	}

	@Override
	public long size() {
		return list.size();
	}

	@Override
	public int schemaSize() {
		return list.isEmpty() ? 0 : list.get(0).size();
	}

	@Override
	public T schema(int index) {
		return list.get(index);
	}

	@Override
	public Class<T> schemaClass() {
		return schemaClass;
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public List<T> elements() {
		return list;
	}
}

package io.intino.alexandria.led;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public interface IndexedLed<T extends Schema> extends Led<T> {
	long size();

	int schemaSize();

	T get(int index);

	List<T> getAll();

	static <E extends Schema> IndexedLed<E> of(List<E> list) {
		list.sort(Comparator.comparingLong(Schema::id));
		return ledFromList(list);
	}

	private static <E extends Schema> IndexedLed<E> ledFromList(List<E> list) {
		return new IndexedLed<>() {
			private final List<E> theList = Collections.unmodifiableList(list);

			@Override
			public void close() {
			}

			@Override
			public long size() {
				return theList.size();
			}

			@Override
			public int schemaSize() {
				return list.isEmpty() ? 0: list.get(0).size();
			}

			@Override
			public E get(int index) {
				return theList.get(index);
			}

			@Override
			public List<E> getAll() {
				return theList;
			}

			@Override
			public Stream<E> stream() {
				return theList.stream();
			}
		};
	}
}
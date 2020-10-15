package io.intino.alexandria.led;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public interface IndexedLed<T extends Schema> extends Led<T> {
	static <E extends Schema> IndexedLed<E> of(int elementSize, List<E> list) {
		list.sort(Comparator.comparingLong(Schema::id));
		return ledFromList(elementSize, list);
	}

	long count();

	default long sizeInBytes() {
		return count() * elementSize();
	}

	T get(int index);

	List<T> getAll();

	private static <E extends Schema> IndexedLed<E> ledFromList(int elementSize, List<E> list) {
		return new IndexedLed<>() {
			private final List<E> theList = Collections.unmodifiableList(list);

			@Override
			public void close() {
			}

			@Override
			public long count() {
				return theList.size();
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
			public int elementSize() {
				return elementSize;
			}

			@Override
			public Stream<E> stream() {
				return theList.stream();
			}
		};
	}
}
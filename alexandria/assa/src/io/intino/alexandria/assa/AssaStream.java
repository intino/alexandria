package io.intino.alexandria.assa;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingLong;

public interface AssaStream<T extends Serializable> extends Iterator<AssaStream.Item<T>> {

	int size();

	Item<T> next();

	boolean hasNext();

	interface Item<T> {
		long key();

		T object();
	}

	default void save(String name, File file) throws IOException {
		new AssaWriter(file).save(name, this);
	}

	class Merge {
		public static <T extends Serializable> AssaStream of(List<AssaStream<T>> streams) {
			return new AssaStream<T>() {
				private Item[] items = items();
				private Item<T> next = advancing(current());

				@Override
				public Item<T> next() {
					Item<T> current = this.next;
					this.next = advancing(current());
					return current;
				}

				@Override
				public boolean hasNext() {
					return next != null;
				}

				public int size() {
					return streams.stream().mapToInt(AssaStream::size).sum();
				}

				private Item[] items() {
					return streams.stream()
							.map(AssaStream::next)
							.toArray(Item[]::new);
				}

				@SuppressWarnings("unchecked")
				private Item<T> advancing(Item current) {
					for (int i = 0; i < items.length; i++) {
						Item cursor = items[i];
						if (cursor == null || cursor.key() != current.key()) continue;
						items[i] = streams.get(i).hasNext() ? streams.get(i).next() : null;
					}
					return current;
				}

				private Item current() {
					return stream(items)
							.filter(Objects::nonNull)
							.min(comparingLong(Item::key))
							.orElse(null);
				}

			};
		}

	}

}

package io.intino.alexandria.assa;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Comparator.comparingLong;

public interface AssaStream<T extends Serializable> extends Iterator<AssaStream.Item<T>> {

	int size();

	Item<T> next();

	boolean hasNext();

	void close();

	default void save(String name, File file) throws IOException {
		new AssaWriter(file).save(name, this);
	}

	interface Item<T> {
		long key();

		T object();
	}

	class Merge {
		public static <T extends Serializable> AssaStream of(List<AssaStream<T>> cursors) {
			return new AssaStream<T>() {
				private List<Item<T>> items = new ArrayList<>();
				private int index = 0;

				{
					for (AssaStream<T> cursor : cursors) {
						while (cursor.hasNext()) items.add(cursor.next());
						cursor.close();
					}
					items.sort(comparingLong(Item::key));
				}

				@Override
				public Item<T> next() {
					return items.get(index++);
				}

				@Override
				public boolean hasNext() {
					return index < items.size() - 1;
				}

				@Override
				public void close() {
				}

				public int size() {
					return items.size();
				}
			};
		}

	}

}

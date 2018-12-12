package io.intino.alexandria.assa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Comparator.comparingLong;

public interface AssaStream extends Iterator<AssaStream.Item> {

	int size();

	Item next();

	boolean hasNext();

	void close();

	interface Item {
		long key();

		String value();
	}

	class Merge {
		public static AssaStream of(List<AssaStream> cursors) {
			return new AssaStream() {
				private List<Item> items = new ArrayList<>();
				private int index = 0;

				{
					for (AssaStream cursor : cursors) {
						while (cursor.hasNext()) items.add(cursor.next());
						cursor.close();
					}
					items.sort(comparingLong(Item::key));
				}

				@Override
				public Item next() {
					return items.get(index++);
				}

				@Override
				public boolean hasNext() {
					return index < items.size();
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

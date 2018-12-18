package io.intino.alexandria.assa;

import java.util.*;

import static java.util.stream.Collectors.toList;

public interface AssaStream extends Iterator<AssaStream.Item> {

	Item next();

	boolean hasNext();

	void close();

	interface Item {
		long key();

		List<String> value();
	}

	class Merge {
		public static AssaStream of(List<AssaStream> cursors) {
			return new AssaStream() {

				private List<AssaStreamWithCurrent> streams = cursors.stream().map(AssaStreamWithCurrent::new).collect(toList());
				private Comparator<AssaStreamWithCurrent> comparator = Comparator.comparing(a -> a.current().key());

				{
					streams.forEach(AssaStreamWithCurrent::next);
					streams.sort(comparator);
				}

				@Override
				public Item next() {
					long key = streams.get(0).current.key();
					List<Item> items = itemsWith(key);
					updateStreams(items);
					return compositeItem(items);
				}

				private void updateStreams(List<Item> items) {
					for (int i = 0; i < items.size(); i++) {
						AssaStreamWithCurrent stream = streams.remove(0);
						stream.next();
						int index = Collections.binarySearch(streams, stream, comparator);
						index = index < 0 ? (index + 1) * -1 : index;
						streams.add(index, stream);
					}
				}

				private List<Item> itemsWith(long key) {
					List<Item> items = new ArrayList<>();
					for (AssaStreamWithCurrent stream1 : streams) {
						if (stream1.current.key() != key) break;
						items.add(stream1.current);
					}
					return items;
				}

				private Item compositeItem(List<Item> items) {
					return new Item() {
						@Override
						public long key() {
							return items.get(0).key();
						}

						@Override
						public List<String> value() {
							List<String> result = new ArrayList<>();
							for (Item item : items) result.addAll(item.value());
							return new ArrayList<>(result);
						}
					};
				}

				@Override
				public boolean hasNext() {
					return !streams.isEmpty() && streams.get(0).current().key() != Long.MAX_VALUE;
				}

				@Override
				public void close() {
					streams.forEach(AssaStreamWithCurrent::close);
				}

				class AssaStreamWithCurrent {
					private final AssaStream stream;
					private Item current = null;

					AssaStreamWithCurrent(AssaStream stream) {
						this.stream = stream;
					}

					Item current() {
						return current;
					}

					Item next() {
						return current = hasNext() ? stream.next() : nullItem();
					}

					private Item nullItem() {
						return new Item() {
							@Override
							public long key() {
								return Long.MAX_VALUE;
							}

							@Override
							public List<String> value() {
								return null;
							}
						};
					}

					boolean hasNext() {
						return stream.hasNext();
					}

					void close() {
						stream.close();
					}
				}
			};
		}

	}

}

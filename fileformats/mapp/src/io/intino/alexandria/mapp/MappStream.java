package io.intino.alexandria.mapp;

import java.util.*;

import static java.util.stream.Collectors.toList;

public interface MappStream extends Iterator<MappStream.Item> {

	boolean hasNext();

	Item next();

	void close();

	interface Item {
		long key();

		String value();
	}

	class Merge {
		private Merge() {

		}

		public static MappStream of(List<MappStream> cursors) {
			return new MappStream() {

				private List<MappStreamWithCurrent> streams = cursors.stream().map(MappStreamWithCurrent::new).collect(toList());
				private Comparator<MappStreamWithCurrent> comparator = Comparator.comparing(a -> a.current().key());

				{
					streams.forEach(MappStreamWithCurrent::next);
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
						MappStreamWithCurrent stream = streams.remove(0);
						stream.next();
						int index = Collections.binarySearch(streams, stream, comparator);
						index = index < 0 ? (index + 1) * -1 : index;
						streams.add(index, stream);
					}
				}

				private List<Item> itemsWith(long key) {
					List<Item> items = new ArrayList<>();
					for (MappStreamWithCurrent stream : streams) {
						if (stream.current.key() != key) break;
						items.add(stream.current);
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
						public String value() {
							List<String> result = new ArrayList<>();
							for (Item item : items) result.add(item.value());
							return String.join("\n", result);
						}
					};
				}

				@Override
				public boolean hasNext() {
					return !streams.isEmpty() && streams.get(0).current().key() != Long.MAX_VALUE;
				}

				@Override
				public void close() {
					streams.forEach(MappStreamWithCurrent::close);
				}

				class MappStreamWithCurrent {
					private final MappStream stream;
					private Item current = null;

					MappStreamWithCurrent(MappStream stream) {
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
							public String value() {
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

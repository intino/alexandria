package io.intino.alexandria;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public interface Ledger<T extends Item> extends Iterable<T> {

	Stream<T> stream();
	Stream<T> parallelStream();

	class Filter<T extends Item> implements Ledger<T> {

		private final Ledger<T> ledger;
		private final Predicate<T> predicate;

		public Filter(Ledger<T> ledger, Predicate<T> predicate) {
			this.ledger = ledger;
			this.predicate = predicate;
		}

		@Override
		public Stream<T> stream() {
			return ledger.stream().filter(predicate);
		}

		@Override
		public Stream<T> parallelStream() {
			return ledger.parallelStream().filter(predicate);
		}

		@Override
		public Iterator<T> iterator() {
			return stream().iterator();
		}
	}

	class Join<T extends Item> implements Ledger<T> {
		private final List<Ledger<T>> ledgers;

		public Join(List<Ledger<T>> ledgers) {
			this.ledgers = ledgers;
		}

		@Override
		public Stream<T> stream() {
			return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
		}

		@Override
		public Stream<T> parallelStream() {
			return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), true);
		}

		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				private List<Iterator<T>> iterators = iterators();
				private List<T> next = first();

				@Override
				public boolean hasNext() {
					return next.stream().anyMatch(Objects::nonNull);
				}

				@Override
				public T next() {
					return itemWithId(min());
				}

				private long min() {
					return next.stream().filter(Objects::nonNull).mapToLong(Item::id).min().orElse(-1);
				}

				private T itemWithId(long min) {
					int index = 0;
					for (T item : next) {
						if (item != null && item.id() == min) {
							advance(index);
							return item;
						}
						index++;
					}
					return null;
				}

				private void advance(int index) {
					Iterator<T> iterator = iterators.get(index);
					next.set(index, iterator.hasNext() ? iterator.next() : null);
				}

				private List<Iterator<T>> iterators() {
					return ledgers.stream().map(Iterable::iterator).collect(toList());
				}

				private List<T> first() {
					return iterators.stream().map(i->i.hasNext() ? i.next() : null).collect(toList());
				}
			};
		}

	}

}

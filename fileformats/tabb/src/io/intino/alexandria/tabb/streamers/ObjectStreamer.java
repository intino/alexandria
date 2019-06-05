package io.intino.alexandria.tabb.streamers;

import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.ColumnStreamer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyIterator;
import static java.util.stream.Collectors.toList;

public class ObjectStreamer<T> implements ColumnStreamer {
	private final Iterator<T> iterator;
	private final List<Selector<T>> selectors;
	private Iterator<T> items;
	private List<ColumnStream> streams;

	private long key;
	private T current;

	public ObjectStreamer(Iterator<T> iterator) {
		this.iterator = iterator;
		this.selectors = new ArrayList<>();
		this.key = -1;
	}

	public <R> ObjectStreamer<R> reduceWith(Reducer<T, R> reducer) {
		return new ObjectStreamer<>(new Splitter<>(iterator, reducer).iterator());
	}

	public ObjectStreamer<T> add(Selector<T> selector) {
		this.selectors.add(selector);
		return this;
	}

	@Override
	public ColumnStream[] get() {
		return streams().toArray(new ColumnStream[0]);
	}

	private List<ColumnStream> streams() {
		if (streams != null) return streams;
		this.items = iterator;
		return streams = selectors.stream().map(this::selectorStream).collect(toList());
	}

	private ColumnStream selectorStream(Selector<T> selector) {
		return new ColumnStream() {
			private long key;

			@Override
			public String name() {
				return selector.name();
			}

			@Override
			public Type type() {
				return selector.type();
			}

			@Override
			public boolean hasNext() {
				return getNext(key);
			}

			@Override
			public void next() {
				key++;
			}

			@Override
			public Long key() {
				return key;
			}

			@Override
			public Object value() {
				return selector.select(getCurrent(key));
			}
		};
	}

	private boolean getNext(long key) {
		return key < this.key || items.hasNext();
	}

	private T getCurrent(long key) {
		if (key > this.key) {
			this.key = key;
			this.current = items.next();
		}
		return this.current;
	}

	public interface Selector<T> {
		String name();

		Type type();

		Object select(T t);
	}

	public interface Reducer<T, R> {
		boolean canAdd(T current);

		void add(T item);
		Iterator<R> items();

		void clear();
	}

	static class Splitter<T, R> implements Iterable<R> {
		private final Iterator<T> iterator;
		private final Reducer<T, R> reducer;
		private T next = null;

		Splitter(Iterator<T> iterator, Reducer<T, R> reducer) {
			this.iterator = iterator;
			this.reducer = reducer;
		}

		@Override
		@SuppressWarnings("NullableProblems")
		public Iterator<R> iterator() {
			return new Iterator<R>() {
				private Iterator<R> split = emptyIterator();

				@Override
				public boolean hasNext() {
					return split.hasNext() || next != null;
				}

				@Override
				public R next() {
					while (!split.hasNext()) nextSplit();
					return split.next();

				}

				private void nextSplit() {
					reducer.clear();
					while (next != null) {
						reducer.add(next);
						next = iterator.hasNext() ? iterator.next() : null;
						if (reducer.canAdd(next)) break;
					}
					split = reducer.items();
				}
			};
		}


	}

}

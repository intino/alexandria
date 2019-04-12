package io.intino.alexandria.tabb;

import io.intino.alexandria.tabb.ColumnStream.Mode;
import io.intino.alexandria.tabb.ColumnStream.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ColumnStreamer<T> {
	private final Iterator<T> iterator;
	private final List<Selector<T>> selectors;
	private Combiner<T> combiner;

	private Splitter<T> splitter;
	private Iterator<T> split;
	private List<ColumnStream> streams;
	private long key = -1;
	private T current;

	public ColumnStreamer(Iterator<T> iterator) {
		this.iterator = iterator;
		this.selectors = new ArrayList<>();
		this.splitter = new Splitter<>(iterator, new NullCombiner<>());
	}

	public ColumnStreamer<T> add(Selector<T> selector) {
		this.selectors.add(selector);
		return this;
	}

	public ColumnStreamer<T> groupWith(Combiner<T> combiner) {
		this.combiner = combiner;
		return this;
	}

	public List<ColumnStream> streams() {
		if (streams != null) return streams;
		this.splitter = new Splitter<>(iterator, combiner);
		this.split = Collections.emptyIterator();
		return streams = selectors.stream().map(this::selectorStream).collect(toList());
	}

	private ColumnStream selectorStream(Selector<T> selector) {
		return new ColumnStream() {
			@Override
			public String name() {
				return selector.name();
			}

			@Override
			public Type type() {
				return selector.type();
			}

			@Override
			public Mode mode() {
				return selector.mode();
			}

			@Override
			public boolean hasNext() {
				return split.hasNext() || splitter.hasNext();
			}

			@Override
			public void next() {
				key++;
				if (!split.hasNext()) split = splitter.next();
				current = split.next();
			}

			@Override
			public Long key() {
				return key;
			}

			@Override
			public byte[] value() {
				return selector.select(current);
			}
		};
	}

	public interface Selector<T> {
		String name();

		Type type();

		Mode mode();

		byte[] select(T t);
	}

	private interface Breaker<T> {
		boolean canBeAdded(T current);
	}

	private interface Merger<T> {
		void clear();

		void add(T item);

		Iterator<T> items();

	}

	public interface Combiner<T> extends Breaker<T>, Merger<T> {
	}

	public static class Splitter<T> {
		private final Iterator<T> iterator;
		private final Breaker<T> breaker;
		private final Merger<T> merger;
		private T next = null;


		public Splitter(Iterator<T> iterator, Combiner<T> combiner) {
			this.iterator = iterator;
			this.breaker = combiner;
			this.merger = combiner;
			this.start();
		}

		private void start() {
			next = iterator.hasNext() ? iterator.next() : null;
		}

		public boolean hasNext() {
			return next != null;
		}

		public Iterator<T> next() {
			merger.clear();
			do {
				merger.add(next);
				next = iterator.hasNext() ? iterator.next() : null;
			}
			while (next == null || breaker.canBeAdded(next));
			return merger.items();
		}
	}

	private static class NullCombiner<T> implements Combiner<T> {
		private List<T> items;

		@Override
		public void clear() {
			items = new ArrayList<>();
		}

		@Override
		public boolean canBeAdded(T current) {
			return true;
		}

		@Override
		public void add(T item) {
			items.add(item);
		}

		@Override
		public Iterator<T> items() {
			return items.iterator();
		}

	}


}

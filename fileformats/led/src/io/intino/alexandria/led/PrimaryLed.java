package io.intino.alexandria.led;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class PrimaryLed<T extends Item> implements Led<T> {
	private final List<T> items;

	PrimaryLed(List<T> items) {
		this.items = items;
	}

	public int size() {
		return items.size();
	}

	@SuppressWarnings("unused")
	public <X extends Aggregation<X, T>> Aggregator<X> aggregate(Class<X> clazz) {
		return new Aggregator<>();
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public Stream<T> stream() {
		return items.stream();
	}

	@Override
	public Stream<T> parallelStream() {
		return items.parallelStream();
	}

	@Override
	public int hashCode() {
		return Objects.hash(items);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) return false;
		return this == object || Objects.equals(items, ((PrimaryLed<?>) object).items);
	}

	public static <T extends Item> PrimaryLed<T> load(Stream<T> stream) {
		return new PrimaryLed<>(stream.sorted(Comparator.comparingLong(Item::id)).collect(toList()));
	}

	public static <T extends Item> PrimaryLed<T> load(Class<T> clazz, InputStream inputStream) {
		return new LedReader(inputStream).read(clazz);
	}

	public class Aggregator<X extends Aggregation<X, T>> implements Iterable<X> {

		private final List<X> aggregations;

		Aggregator() {
			this.aggregations = new ArrayList<>();
		}

		public Aggregator<X> set(X aggregation) {
			aggregations.add(aggregation);
			return this;
		}

		public Aggregator<X> execute() {
			items.forEach(this::aggregate);
			return this;
		}

		@Override
		public Iterator<X> iterator() {
			return aggregations.iterator();
		}

		private void aggregate(T item) {
			aggregations.stream().parallel()
					.filter(a -> a.predicate().test(item))
					.forEach(a -> a.add(item));
		}

		public List<X> aggregations() {
			return aggregations;
		}

		public X aggregation(int index) {
			return aggregations.get(index);
		}
	}
}
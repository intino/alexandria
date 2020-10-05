package io.intino.alexandria;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class PrimaryLedger<T extends Item> implements Ledger<T> {
	private List<T> items;

	private PrimaryLedger(List<T> items) {
		this.items = items;
	}

	public int size() {
		return items.size();
	}

	public static <T extends Item> PrimaryLedger<T> load(Stream<T> stream) {
		return new PrimaryLedger<>(stream.sorted(Comparator.comparingLong(Item::id)).collect(toList()));
	}

	public static <T extends Item> PrimaryLedger<T> load(Class<T> clazz, InputStream inputStream) {
		Kryo kryo = new Kryo();
		kryo.register(clazz);
		List<T> items = new ArrayList<>();
		try (Input input = new Input(new BufferedInputStream(inputStream))) {
			while (!input.eof()) items.add(kryo.readObject(input, clazz));
		}
		return new PrimaryLedger<>(items);
	}

	public void store(OutputStream outputStream) {
		Kryo kryo = new Kryo();
		kryo.register(items.get(0).getClass());
		try (Output output = new Output(new BufferedOutputStream(outputStream))) {
			items.forEach(i->kryo.writeObject(output,i));
		}
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) return false;
		return this == object || Objects.equals(items, ((PrimaryLedger<?>) object).items);
	}

	@Override
	public int hashCode() {
		return Objects.hash(items);
	}

	@SuppressWarnings("unused")
	public <X extends Aggregation<X,T>> Aggregator<X> aggregate(Class<X> clazz) {
		return new Aggregator<>();
	}
	public class Aggregator<X extends Aggregation<X,T>> implements Iterable<X> {
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
					.filter(a->a.predicate().test(item))
					.forEach(a->a.add(item));
		}

		public List<X> aggregations() {
			return aggregations;
		}

		public X aggregation(int index) {
			return aggregations.get(index);
		}
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



}

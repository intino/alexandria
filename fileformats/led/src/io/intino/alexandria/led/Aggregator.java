package io.intino.alexandria.led;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Aggregator<AGGR extends Aggregation<AGGR, T>, T extends Schema> implements Iterable<AGGR> {

	public static <AGGR_ extends Aggregation<AGGR_, T_>, T_ extends Schema> Aggregator<AGGR_, T_> of(Led<T_> ledger) {
		return new Aggregator<>(ledger);
	}

	public static <AGGR_ extends Aggregation<AGGR_, T_>, T_ extends Schema> Aggregator<AGGR_, T_> of(
			Class<AGGR_> aggregationClass, Led<T_> ledger) {
		return new Aggregator<>(ledger);
	}

	private final Led<T> ledger;
	private final List<AGGR> aggregations;

	public Aggregator(Led<T> ledger) {
		this.ledger = ledger;
		this.aggregations = new ArrayList<>();
	}

	public Aggregator<AGGR, T> set(AGGR aggregation) {
		aggregations.add(aggregation);
		return this;
	}

	public Aggregator<AGGR, T> execute() {
		ledger.forEach(this::aggregate);
		return this;
	}

	@Override
	public Iterator<AGGR> iterator() {
		return aggregations.iterator();
	}

	private void aggregate(T item) {
		aggregations.stream()
				.parallel()
				.filter(a -> a.predicate().test(item))
				.forEach(a -> a.add(item));
	}

	public List<AGGR> aggregations() {
		return aggregations;
	}

	public AGGR aggregation(int index) {
		return aggregations.get(index);
	}
}
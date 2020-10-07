package io.test.led;

import io.intino.alexandria.led.Aggregation;

import java.util.function.Predicate;

public class TestAggregation implements Aggregation<TestAggregation,TestItem> {
	private final String label;
	private final Predicate<TestItem> predicate;
	private int count = 0;
	private long i = 0;
	private double d;

	public TestAggregation(String label, Predicate<TestItem> predicate) {
		this.label = label;
		this.predicate = predicate;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public Predicate<TestItem> predicate() {
		return predicate;
	}

	@Override
	public TestAggregation add(TestItem item) {
		count++;
		i += item.i();
		d += item.d();
		return this;
	}

	public long i() {
		return i;
	}

	public double d() {
		return d;
	}

	@Override
	public int count() {
		return count;
	}
}

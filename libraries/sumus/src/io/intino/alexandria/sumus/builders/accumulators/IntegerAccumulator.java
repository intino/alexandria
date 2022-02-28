package io.intino.alexandria.sumus.builders.accumulators;

import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.builders.CellBuilder;

import java.util.List;

public class IntegerAccumulator extends BuilderAccumulator {
	private int count;
	private long sum;
	private long min;
	private long max;

	public IntegerAccumulator(String name) {
		super(name);
		this.count = 0;
		this.sum = 0;
		this.min = Long.MAX_VALUE;
		this.max = Long.MIN_VALUE;
	}

	@Override
	public void add(Object value) {
		if (value == null) return;
		this.count++;
		this.sum = this.sum + (long) value;
		this.min = Math.min(this.min, (long) value);
		this.max = Math.max(this.max, (long) value);
	}

	@Override
	public List<Cube.Indicator> indicators() {
		return count == 0 ? super.indicators() : List.of(
				new CellBuilder.Indicator("_count(" + name + ")", count),
				new CellBuilder.Indicator("_sum(" + name + ")", sum),
				new CellBuilder.Indicator("_avg(" + name + ")", (double) sum / count),
				new CellBuilder.Indicator("_min(" + name + ")", min),
				new CellBuilder.Indicator("_max(" + name + ")", max)
		);
	}
}

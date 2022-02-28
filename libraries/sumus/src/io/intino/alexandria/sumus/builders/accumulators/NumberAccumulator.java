package io.intino.alexandria.sumus.builders.accumulators;

import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.builders.CellBuilder;

import java.util.List;

public class NumberAccumulator extends BuilderAccumulator {
	private int count;
	private double sum;
	private double min;
	private double max;

	public NumberAccumulator(String name) {
		super(name);
		this.count = 0;
		this.sum = 0;
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
	}

	public void add(Object value) {
		if (value == null) return;
		this.count++;
		this.sum = this.sum + (double) value;
		this.min = Math.min(this.min, (double) value);
		this.max = Math.max(this.max, (double) value);
	}

	@Override
	public List<Cube.Indicator> indicators() {
		return count == 0 ? super.indicators() : List.of(
				new CellBuilder.Indicator("_count(" + name + ")", count),
				new CellBuilder.Indicator("_sum(" + name + ")", sum),
				new CellBuilder.Indicator("_avg(" + name + ")", sum / count),
				new CellBuilder.Indicator("_min(" + name + ")", min),
				new CellBuilder.Indicator("_max(" + name + ")", max)
		);
	}
}

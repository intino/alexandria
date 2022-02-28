package io.intino.alexandria.sumus.builders.accumulators;

import io.intino.alexandria.sumus.builders.Accumulator;
import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.builders.CellBuilder;

import java.util.List;

public abstract class BuilderAccumulator implements Accumulator {
	public final String name;
	protected int count;

	public BuilderAccumulator(String name) {
		this.name = name;
		this.count = 0;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<Cube.Indicator> indicators() {
		return List.of(
				new CellBuilder.Indicator("_count(" + name + ")", count)
		);
	}

}

package io.intino.alexandria.sumus.builders.accumulators;

public class CountAccumulator extends BuilderAccumulator {

	public CountAccumulator(String name) {
		super(name);
	}

	@Override
	public void add(Object value) {
		this.count++;
	}

}

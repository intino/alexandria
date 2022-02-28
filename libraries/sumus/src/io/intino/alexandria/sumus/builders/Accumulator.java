package io.intino.alexandria.sumus.builders;

import io.intino.alexandria.sumus.Cube;

import java.util.List;

public interface Accumulator {
	String name();
	void add(Object value);
	List<Cube.Indicator> indicators();
}

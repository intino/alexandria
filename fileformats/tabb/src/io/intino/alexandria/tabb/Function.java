package io.intino.alexandria.tabb;

import io.intino.alexandria.Timetag;

public interface Function<R, T> {
	T apply(Long key, R value, Timetag timetag);
}

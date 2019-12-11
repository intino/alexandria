package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake.EventStore.Tank;

import java.util.Collections;
import java.util.List;

public interface SessionSealer {

	default void seal() {
		seal(Collections.emptyList());
	}

	void seal(List<Tank> avoidSorting);
}

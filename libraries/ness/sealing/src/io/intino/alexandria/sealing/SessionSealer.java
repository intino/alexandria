package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.Event;

import java.util.Collections;
import java.util.List;

public interface SessionSealer {

	default void seal() {
		seal(Collections.emptyList());
	}

	void seal(List<Datalake.Store.Tank<? extends Event>> avoidSorting);
}

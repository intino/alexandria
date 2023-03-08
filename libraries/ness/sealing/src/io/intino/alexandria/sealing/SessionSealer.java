package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.Event;

import java.util.function.Predicate;

public interface SessionSealer {

	default void seal() {
		seal(t->true);
	}

	void seal(Predicate<Datalake.Store.Tank<? extends Event>> sortingPolicy);
}

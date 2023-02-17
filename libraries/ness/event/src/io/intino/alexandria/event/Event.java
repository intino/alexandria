package io.intino.alexandria.event;

import java.time.Instant;

public interface Event {
	Instant ts();

	String ss();
}

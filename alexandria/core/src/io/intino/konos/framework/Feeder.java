package io.intino.konos.framework;

import io.intino.ness.inl.Message;

import java.util.List;

public abstract class Feeder {

	public abstract List<String> eventTypes();

	public abstract List<Sensor> sensors();

	public abstract boolean feed(Message event);

	public boolean fits(List<String> eventTypes) {
		return eventTypes().containsAll(eventTypes);
	}

}

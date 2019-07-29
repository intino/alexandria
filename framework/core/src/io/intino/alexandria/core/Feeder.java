package io.intino.alexandria.core;


import io.intino.alexandria.message.Message;

import java.util.List;

public abstract class Feeder {

	public abstract List<String> eventTypes();

	public abstract List<Sensor> sensors();

	public abstract void feed(Message event);

	public boolean fits(List<String> eventTypes) {
		return eventTypes().containsAll(eventTypes);
	}

}

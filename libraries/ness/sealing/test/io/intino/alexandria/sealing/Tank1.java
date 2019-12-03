package io.intino.alexandria.sealing;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.message.Message;

public class Tank1 extends Event {

	Tank1(Event event) {
		this(event.toMessage());
	}

	public Tank1(Message message) {
		super(message);
	}

	Tank1() {
		super(Tank1.class.getSimpleName());
	}

	int entries() {
		return message.get("entries").asInteger();
	}

	Tank1 entries(int v) {
		message.set("entries", v);
		return this;
	}
}

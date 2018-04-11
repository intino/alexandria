package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.Datalake.Tank;
import io.intino.konos.datalake.MessageHandler;
import io.intino.konos.jms.TopicConsumer;
import io.intino.ness.inl.Message;

public class FSTank implements Tank {
	private final String name;
	private final FSDatalake datalake;
	private MessageHandler handler;

	public FSTank(String name, FSDatalake datalake) {
		this.name = name;
		this.datalake = datalake;
	}

	@Override
	public void handler(MessageHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handle(Message message) {
		handler.handle(message);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public void feed(Message message) {
		datalake.drop(name, message);
	}

	@Override
	public void drop(Message message) {
		datalake.drop(name, message);
	}

	@Override
	public TopicConsumer flow(String flowID) {
		return null;
	}

	@Override
	public void unregister() {

	}
}

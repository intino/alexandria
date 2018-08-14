package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.Datalake.Tank;
import io.intino.konos.datalake.MessageHandler;
import io.intino.konos.datalake.jms.JMSTank;
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
	public Tank handler(MessageHandler handler) {
		this.handler = handler;
		return this;
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
	public void feed(Message[] messages) {
		datalake.put(name, messages);
	}

	@Override
	public void put(Message[] messages) {
		datalake.put(name, messages);
	}

	@Override
	public Tank batchSession(int blockSize) {
		datalake.tank(name).batch(blockSize);
		return this;
	}

	@Override
	public Tank endBatch() {
		datalake.tank(name).endBatch();
		return this;
	}

	@Override
	public TopicConsumer flow(String flowID) {
		return null;
	}

	@Override
	public void unregister() {

	}
}

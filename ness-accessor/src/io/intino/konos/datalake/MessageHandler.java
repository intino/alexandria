package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.ness.inl.Message;

public interface MessageHandler extends Consumer {

	void handle(Message message);

	@Override
	default void consume(javax.jms.Message message) {
		handle(MessageTranslator.toInlMessage(message));
	}
}

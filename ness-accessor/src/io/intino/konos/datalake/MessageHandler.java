package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.ness.inl.Message;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;

public interface MessageHandler extends Consumer {

	void handle(Message message);

	@Override
	default void consume(javax.jms.Message message) {
		handle(Message.load(textFrom(message)));
	}

	@SuppressWarnings("Duplicates")
	default String textFrom(javax.jms.Message message) {
		try {
			if (message instanceof BytesMessage) {
				byte[] data = new byte[(int) ((BytesMessage) message).getBodyLength()];
				((BytesMessage) message).readBytes(data);
				return new String(data);
			} else return ((TextMessage) message).getText();
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

}

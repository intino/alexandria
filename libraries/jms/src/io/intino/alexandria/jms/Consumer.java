package io.intino.alexandria.jms;


import io.intino.alexandria.logger.Logger;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public interface Consumer {

	void consume(Message message);

	static String textFrom(Message message) {
		try {
			if (message instanceof BytesMessage) {
				byte[] data = new byte[(int) ((BytesMessage) message).getBodyLength()];
				((BytesMessage) message).readBytes(data);
				return new String(data);
			} else return ((TextMessage) message).getText();
		} catch (JMSException e) {
			Logger.error(e.getMessage(), e);
			return "";
		}
	}

	default String typeOf(String text) {
		return text.contains("\n") ? text.substring(0, text.indexOf("\n")).replace("[", "").replace("]", "") : text;
	}
}

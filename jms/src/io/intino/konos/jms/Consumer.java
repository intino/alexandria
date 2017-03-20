package io.intino.konos.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public interface Consumer {
	void consume(Message message);

	default String textFrom(Message message) {
		try {
			if (message instanceof BytesMessage) {
				byte[] data = new byte[(int) ((BytesMessage) message).getBodyLength()];
				((BytesMessage) message).readBytes(data);
				return new String(data);
			} else return ((TextMessage) message).getText();
		} catch (JMSException e) {
			e.printStackTrace();
			return "";
		}
	}

	default String typeOf(String text) {
		return text.substring(0, text.indexOf("\n")).replace("[", "").replace("]", "");
	}
}

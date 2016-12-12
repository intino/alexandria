package io.intino.pandora.jms;

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
}

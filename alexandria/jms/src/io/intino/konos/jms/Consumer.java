package io.intino.konos.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public interface Consumer {
	Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	void consume(Message message);

	static String textFrom(Message message) {
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

	default String typeOf(String text) {
		return text.contains("\n") ? text.substring(0, text.indexOf("\n")).replace("[", "").replace("]", "") : text;
	}
}

package io.intino.alexandria.jms;

import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class MessageWriter {
	public static javax.jms.Message write(String message) throws JMSException {
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText(message);
		return textMessage;
	}
}

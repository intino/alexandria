package io.intino.konos.jms;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Message;
import javax.jms.MessageNotWriteableException;

public class MessageFactory {

	public static Message textMessage() {
		return new ActiveMQTextMessage();
	}

	public static Message objectMessage() {
		return new ActiveMQObjectMessage();
	}

	public static Message byteMessage() {
		return new ActiveMQBytesMessage();
	}


	public static Message createMessageFor(Object object) {
		try {
			if (object instanceof java.io.File || object instanceof byte[]) return new ActiveMQBytesMessage();
			final ActiveMQTextMessage message = new ActiveMQTextMessage();
			message.setText(new com.google.gson.Gson().toJson(object));
			return message;
		} catch (MessageNotWriteableException e) {
			e.printStackTrace();
		}
		return null;
	}
}

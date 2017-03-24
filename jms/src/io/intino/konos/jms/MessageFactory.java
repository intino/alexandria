package io.intino.konos.jms;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
			if (object instanceof java.io.File || object instanceof byte[]) {
				ActiveMQBytesMessage bytesMessage = new ActiveMQBytesMessage();
				bytesMessage.writeBytes(object instanceof Byte[] ? (byte[]) object : Files.readAllBytes(((File) object).toPath()));
				return bytesMessage;
			}
			final ActiveMQTextMessage message = new ActiveMQTextMessage();
			message.setText(object instanceof String ? object.toString() : new com.google.gson.Gson().toJson(object));
			return message;
		} catch (JMSException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

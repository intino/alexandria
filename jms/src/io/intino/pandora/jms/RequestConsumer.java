package io.intino.pandora.jms;

import com.google.gson.Gson;
import io.intino.pandora.exceptions.PandoraException;

import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface RequestConsumer {

	void consume(Session session, Message message);


	default Destination replyTo(Message request) {
		try {
			return request.getJMSReplyTo();
		} catch (JMSException e) {
			e.printStackTrace();
			return null;
		}
	}

	default String idOf(Message message) {
		try {
			return message.getJMSCorrelationID();
		} catch (JMSException e) {
			e.printStackTrace();
			return "";
		}
	}

	default void response(Session session, Destination destination, Message message) {
		if (session == null || destination == null || message == null) return;
		try {
			new QueueProducer(session, destination).produce(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	default InputStream toInputStream(byte[] content) {
		return new ByteArrayInputStream(content);
	}

	default byte[] toByteArray(InputStream stream) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = stream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			return buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}


	default Message exceptionMessage(Session session, String responseId, PandoraException response) {
		try {
			TextMessage message = session.createTextMessage();
			message.setJMSCorrelationID(responseId);
			message.setText(new Gson().toJson(response));
			return message;
		} catch (JMSException e) {
			e.printStackTrace();
			return null;
		}
	}
}

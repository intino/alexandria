package io.intino.pandora.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

	default File newTemporalFile(byte[] content) {
		try {
			final Path tempFile = Files.createTempFile(null, null);
			Files.write(tempFile, content);
			tempFile.toFile().deleteOnExit();
			return tempFile.toFile();
		} catch (IOException e) {
			return null;
		}
	}

	default byte[] readFile(File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			return new byte[0];
		}
	}
}

package io.intino.alexandria.nessaccesor;

import io.intino.alexandria.nessaccesor.NessAccessor.Stage;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class EventSession implements NessAccessor.EventStore.EventSession {
	private static Logger logger = LoggerFactory.getLogger(EventSession.class);
	private final BufferedWriter writer;

	EventSession(Stage file) {
		writer = new BufferedWriter(new OutputStreamWriter(file.start(Stage.Type.event)));
	}

	@Override
	public void append(String tank, List<Message> messages) {
		for (Message message : messages) write(message);
	}

	private void write(Message message) {
		try {
			writer.write(message.toString() + "\n\n");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void append(String tank, Message... messages) {
		for (Message message : messages) write(message);
	}

	@Override
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}

package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class JmsConsumer {
	final Session session;
	final Destination destination;
	List<Consumer<Message>> listeners;
	MessageConsumer consumer;

	JmsConsumer(Session session, Destination destination) {
		this.session = session;
		this.destination = destination;
		this.listeners = new ArrayList<>();
	}

	public void listen(Consumer<Message> listener) {
		try {
			listeners.add(listener);
			if (this.consumer == null) {
				this.consumer = session.createConsumer(destination);
				consumer.setMessageListener(m -> listeners.forEach(l -> l.accept(m)));
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

    public List<Consumer<Message>>  listeners() {
        return Collections.unmodifiableList(listeners);
    }

    public void removeListener(Consumer<Message> listener) {
		listeners.remove(listener);
	}

	public void close() {
		try {
			consumer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}
}

package io.intino.konos.jms;


import javax.jms.*;
import java.util.logging.Level;

import static java.util.logging.Logger.getGlobal;

public abstract class Producer {


	protected final Session session;
	private Destination destination;


	public Producer(Session session, Destination destination) {
		this.session = session;
		this.destination = destination;
	}

	public void produce(Message message) {
		try {
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(message);
			producer.close();
		} catch (Exception e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}

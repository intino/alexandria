package io.intino.konos.jms;


import javax.jms.*;

public abstract class Producer {


	protected final Session session;
	private Destination destination;


	public Producer(Session session, Destination queue) {
		this.session = session;
		this.destination = queue;
	}

	public void produce(Message message) {
		try {
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(message);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}
}

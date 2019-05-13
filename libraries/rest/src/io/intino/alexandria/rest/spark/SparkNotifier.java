package io.intino.alexandria.rest.spark;

import io.intino.alexandria.rest.pushservice.MessageCarrier;

public class SparkNotifier {
	private MessageCarrier carrier;

	public SparkNotifier(MessageCarrier carrier) {
		this.carrier = carrier;
	}

	public void notify(String message) {
		carrier.notifyClient(message);
	}

}
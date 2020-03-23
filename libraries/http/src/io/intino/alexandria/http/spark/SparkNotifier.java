package io.intino.alexandria.http.spark;

import io.intino.alexandria.http.pushservice.MessageCarrier;

public class SparkNotifier {
	private MessageCarrier carrier;

	public SparkNotifier(MessageCarrier carrier) {
		this.carrier = carrier;
	}

	public void notify(String message) {
		carrier.notifyClient(message);
	}

}
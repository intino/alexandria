package io.intino.alexandria.http.server;

import io.intino.alexandria.http.pushservice.MessageCarrier;

public class AlexandriaHttpNotifier {
	private MessageCarrier carrier;

	public AlexandriaHttpNotifier(MessageCarrier carrier) {
		this.carrier = carrier;
	}

	public void notify(String message) {
		carrier.notifyClient(message);
	}

}
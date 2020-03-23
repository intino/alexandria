package io.intino.alexandria.http.pushservice;

public class MessageCarrier {
	private final Session session;
	private final Client client;
	private final PushService service;

	public MessageCarrier(PushService service, Session session, Client client) {
		this.session = session;
		this.client = client;
		this.service = service;
	}

	public void notifyAll(String message) {
		service.pushBroadcast(message);
	}

	public void notifySession(String message) {
		service.pushToSession(session, message);
	}

	public void notifyClient(String message) {
		service.pushToClient(client, message);
	}
}

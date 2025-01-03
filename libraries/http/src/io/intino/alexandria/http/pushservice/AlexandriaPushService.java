package io.intino.alexandria.http.pushservice;

import io.intino.alexandria.http.server.AlexandriaHttpClient;
import io.intino.alexandria.http.server.AlexandriaHttpSession;

public class AlexandriaPushService extends PushService<Session<Client>, Client> {

	@Override
	public Session createSession(String id) {
		return new AlexandriaHttpSession(id);
	}

	@Override
	public Client createClient(org.eclipse.jetty.websocket.api.Session session) {
		return new AlexandriaHttpClient(session);
	}

}

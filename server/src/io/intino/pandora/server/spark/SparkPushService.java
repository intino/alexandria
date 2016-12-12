package io.intino.pandora.server.spark;

import io.intino.pandora.server.pushservice.Client;
import io.intino.pandora.server.pushservice.Session;

public class SparkPushService extends PushService<Session<Client>, Client> {

	@Override
	public Session createSession(String id) {
		return new SparkSession(id);
	}

	@Override
	public Client createClient(org.eclipse.jetty.websocket.api.Session session) {
		return new SparkClient(session);
	}

}

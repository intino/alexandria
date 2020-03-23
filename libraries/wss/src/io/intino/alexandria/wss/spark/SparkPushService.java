package io.intino.alexandria.wss.spark;

import io.intino.alexandria.wss.Client;
import io.intino.alexandria.wss.PushService;
import io.intino.alexandria.wss.pushservice.Session;

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

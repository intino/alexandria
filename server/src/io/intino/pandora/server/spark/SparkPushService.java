package io.intino.pandora.server.spark;

import io.intino.pandora.server.pushservice.AdapterProxy;
import io.intino.pandora.server.pushservice.Client;
import io.intino.pandora.server.pushservice.Session;
import io.intino.pandora.server.pushservice.SessionManager;

public class SparkPushService extends PushService<Session, Client> {

	public SparkPushService(AdapterProxy adapterProxy, SessionManager sessionManager) {
		super(adapterProxy, sessionManager);
	}

	@Override
	public Session createSession(String id) {
		return new SparkSession(id);
	}

	@Override
	public Client createClient(org.eclipse.jetty.websocket.api.Session session) {
		return new SparkClient(session);
	}

}

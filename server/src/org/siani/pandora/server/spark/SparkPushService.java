package org.siani.pandora.server.spark;

import org.siani.pandora.server.pushservice.AdapterProxy;
import org.siani.pandora.server.pushservice.Client;
import org.siani.pandora.server.pushservice.Session;
import org.siani.pandora.server.pushservice.SessionManager;

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

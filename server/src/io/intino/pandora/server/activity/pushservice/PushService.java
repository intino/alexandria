package io.intino.pandora.server.activity.pushservice;

import org.eclipse.jetty.websocket.api.Session;

public class PushService extends io.intino.pandora.server.spark.PushService<ActivitySession<ActivityClient>, ActivityClient> {

	@Override
	public ActivitySession<ActivityClient> createSession(String id) {
		return new ActivitySession(id);
	}

	@Override
	public ActivityClient createClient(Session session) {
		return new ActivityClient(session);
	}
}

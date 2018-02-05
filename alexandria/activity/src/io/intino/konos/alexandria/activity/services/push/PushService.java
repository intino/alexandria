package io.intino.konos.alexandria.activity.services.push;

import org.eclipse.jetty.websocket.api.Session;

public class PushService extends io.intino.konos.alexandria.rest.spark.PushService<ActivitySession<ActivityClient>, ActivityClient> {

	@Override
	public ActivitySession<ActivityClient> createSession(String id) {
		return new ActivitySession(id);
	}

	@Override
	public ActivityClient createClient(Session session) {
		return new ActivityClient(session);
	}
}

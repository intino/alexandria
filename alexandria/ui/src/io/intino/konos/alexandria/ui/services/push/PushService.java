package io.intino.konos.alexandria.ui.services.push;

import org.eclipse.jetty.websocket.api.Session;

public class PushService extends io.intino.konos.alexandria.rest.spark.PushService<UISession, UIClient> {

	@Override
	public UISession createSession(String id) {
		return new UISession(id);
	}

	@Override
	public UIClient createClient(Session session) {
		return new UIClient(session);
	}
}

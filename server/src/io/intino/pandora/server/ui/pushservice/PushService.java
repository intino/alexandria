package io.intino.pandora.server.ui.pushservice;

import io.intino.pandora.server.ui.pushservice.UISession;
import org.eclipse.jetty.websocket.api.Session;

public class PushService extends io.intino.pandora.server.spark.PushService<UISession<UIClient>, UIClient> {

	@Override
	public UISession<UIClient> createSession(String id) {
		return new UISession(id);
	}

	@Override
	public UIClient createClient(Session session) {
		return new UIClient(session);
	}
}

package io.intino.pandora.server.ui.pushservice;

import io.intino.pandora.server.pushservice.AdapterProxy;
import org.eclipse.jetty.websocket.api.Session;
import io.intino.pandora.server.pushservice.SessionManager;

public class PushService extends io.intino.pandora.server.spark.PushService<UISession, UIClient> {

	public PushService(AdapterProxy adapterProxy, SessionManager sessionManager) {
		super(adapterProxy, sessionManager);
	}

	@Override
	public UISession createSession(String id) {
		return new UISession(id);
	}

	@Override
	public UIClient createClient(Session session) {
		return new UIClient(session);
	}
}

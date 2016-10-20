package org.siani.pandora.server.ui.pushservice;

import org.eclipse.jetty.websocket.api.Session;
import org.siani.pandora.server.pushservice.AdapterProxy;
import org.siani.pandora.server.pushservice.SessionManager;

public class PushService extends org.siani.pandora.server.spark.PushService<UISession, UIClient> {

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

package org.siani.pandora.server.web;

import org.siani.pandora.server.core.Message;
import org.siani.pandora.server.displays.MessageCarrier;
import org.siani.pandora.server.services.PushService;
import org.siani.pandora.server.core.Client;
import org.siani.pandora.server.core.Session;

import java.util.Map;

public class WebMessageCarrier implements MessageCarrier {
	private final PushService pushService;

	public WebMessageCarrier(PushService pushService) {
		this.pushService = pushService;
	}

	@Override
	public void notifyAll(String message, Map<String, Object> parameters) {
		pushService.pushBroadcast(new Message(message, parameters));
	}

	@Override
	public void notify(Client client, String message, Map<String, Object> parameters) {
		pushService.pushToClient(client, new Message(message, parameters));
	}

	@Override
	public void notify(Session session, String message, Map<String, Object> parameters) {
		pushService.pushToSession(session, new Message(message, parameters));
	}

}

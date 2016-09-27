package org.siani.pandora.server.web.services;

import org.siani.pandora.server.web.core.SparkClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class PushServiceHandler {
	private static PushService pushService;

	public static void inject(org.siani.pandora.server.services.PushService pushService) {
		PushServiceHandler.pushService = (PushService) pushService;
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		pushService.onOpen(client(session));
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		pushService.onClose(client(session));
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		pushService.onMessage(client(session), message);
	}

	private SparkClient client(Session session) {
		return new SparkClient(session);
	}
}

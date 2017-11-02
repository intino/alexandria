package io.intino.konos.alexandria.foundation.spark;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class PushServiceHandler {
	private static PushService pushService;

	public static void inject(io.intino.konos.alexandria.foundation.pushservice.PushService pushService) {
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

	protected SparkClient client(Session session) {
		return (SparkClient) PushServiceHandler.pushService.createClient(session);
	}
}

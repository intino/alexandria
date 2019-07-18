package io.intino.alexandria.rest.spark;

import io.intino.alexandria.logger.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@WebSocket
public class PushServiceHandler {
	private static PushService pushService;
	private Map<String, SparkClient> clientsMap = new HashMap<>();
	private Map<String, Timer> closeTimersMap = new HashMap<>();
	private static final int CloseTimeout = 1000*60*60*24;
	private static final int CloseGoingAway = 1001;
	private static final int CloseReadEOF = 1006;

	public static void inject(io.intino.alexandria.rest.pushservice.PushService pushService) {
		PushServiceHandler.pushService = (PushService) pushService;
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		String sessionId = id(session);
		if (closeTimersMap.containsKey(sessionId)) {
			cancelClose(session);
			if (clientsMap.containsKey(sessionId)) refreshSession(session);
			else session.close();
			return;
		}
		registerClient(session);
		pushService.onOpen(client(session));
	}

	@OnWebSocketError
	public void onError(Throwable error) {
		Logger.error(error);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		cancelClose(session);
		if (statusCode == CloseGoingAway || statusCode == CloseReadEOF) {
			doClose(client(session));
			return;
		}
		Logger.debug(String.format("WebSocket connection lost. Status code: %d. %s", statusCode, reason));
		doCloseDelayed(session);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		pushService.onMessage(client(session), message);
	}

	protected SparkClient client(Session session) {
		String sessionId = id(session);
		return clientsMap.get(sessionId);
	}

	protected SparkClient registerClient(Session session) {
		String sessionId = id(session);
		SparkClient client = (SparkClient) PushServiceHandler.pushService.createClient(session);
		clientsMap.put(sessionId, client);
		return client;
	}

	private String id(Session session) {
		return SparkClient.clientId(session);
	}

	private void cancelClose(Session session) {
		String sessionId = id(session);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void doCloseDelayed(Session session) {
		SparkClient client = client(session);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				doClose(client);
			}
		}, CloseTimeout);
		closeTimersMap.put(id(session), timer);
	}

	private void doClose(SparkClient client) {
		String sessionId = client.sessionId();
		pushService.onClose(client);
		clientsMap.remove(sessionId);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void refreshSession(Session session) {
		SparkClient client = client(session);
		client.session(session);
		pushService.linkToThread(client);
	}

}

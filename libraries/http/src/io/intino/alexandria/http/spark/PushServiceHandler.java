package io.intino.alexandria.http.spark;

import io.intino.alexandria.logger.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@WebSocket
public class PushServiceHandler {
	private static PushService pushService;
	private Map<String, SparkClient> clientsMap = new HashMap<>();
	private Map<String, Timer> closeTimersMap = new HashMap<>();
	private static final int CloseTimeout = 1000 * 60 * 60 * 24;
	private static final int CloseGoingAway = 1001;
	private static final int CloseReadEOF = 1006;

	public static void inject(io.intino.alexandria.http.pushservice.PushService pushService) {
		PushServiceHandler.pushService = (PushService) pushService;
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		if (client(session) != null) {
			cancelClose(session);
			client(session).session(session);
		} else registerClient(session);

		pushService.onOpen(client(session));
	}

	@OnWebSocketError
	public void onError(Session session, Throwable error) {
		if (error.getMessage() != null) Logger.debug(error.getMessage());
		else Logger.error(error);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		String sessionId = SparkClient.sessionId(session);
		cancelClose(session);
		if (statusCode == CloseGoingAway) {
			Logger.debug(String.format("WebSocket connection lost. Status code: %d. %s", statusCode, reason));
			doClose(sessionId, client(session));
			return;
		}
		doCloseDelayed(session, sessionId);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		if (client(session) == null) {
			try {
				session.disconnect();
			} catch (IOException e) {
				Logger.error(e);
			}
		}
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
		return SparkClient.sessionId(session);
	}

	private void cancelClose(Session session) {
		String sessionId = id(session);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void doCloseDelayed(Session session, String sessionId) {
		SparkClient client = client(session);
		if (client != null) pushService.onCloseScheduled(client);
		Timer timer = new Timer("Push service delayed close");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				doClose(sessionId, client);
			}
		}, CloseTimeout);
		closeTimersMap.put(sessionId, timer);
	}

	private void doClose(String sessionId, SparkClient client) {
		if (client != null) pushService.onClose(client);
		clientsMap.remove(sessionId);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void refreshSession(Session session) {
		SparkClient client = registerClient(session);
		client.session(session);
		pushService.linkToThread(client);
	}

}

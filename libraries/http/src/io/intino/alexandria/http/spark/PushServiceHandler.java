package io.intino.alexandria.http.spark;

import io.intino.alexandria.http.pushservice.Client;
import io.intino.alexandria.logger.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.io.IOException;
import java.util.*;

@WebSocket(maxTextMessageSize = 5 * 1024 * 1024)
public class PushServiceHandler {
	private static PushService pushService;
	private Map<String, List<SparkClient>> clientsMap = new HashMap<>();
	private Map<String, Timer> closeTimersMap = new HashMap<>();
	private static final int CloseTimeout = 1000 * 60 * 60 * 24;
	private static final int CloseGoingAway = 1001;
	private static final int CloseReadEOF = 1006;

	public static void inject(io.intino.alexandria.http.pushservice.PushService pushService) {
		PushServiceHandler.pushService = (PushService) pushService;
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		Client client = client(session);
		if (client != null) {
			cancelClose(session);
			client(session).session(session);
		} else registerClient(session);

		pushService.onOpen(client(session));
	}

	@OnWebSocketError
	public void onError(Session session, Throwable error) {
		String sessionId = SparkClient.sessionId(session);
		try {
			if (closeTimersMap.containsKey(sessionId)) {
				closeTimersMap.get(sessionId).cancel();
				closeTimersMap.remove(sessionId);
			}
		} catch (Throwable ex) {
			Logger.error(ex);
		}
		if (error.getMessage() != null) Logger.debug(error.getMessage());
		else Logger.debug(error.toString());
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
		Client client = client(session);
		if (client == null) {
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
		if (!clientsMap.containsKey(sessionId)) return null;
		String clientId = clientId(session);
		return clientsMap.get(sessionId).stream().filter(c -> c.id().equals(clientId)).findFirst().orElse(null);
	}

	protected SparkClient registerClient(Session session) {
		String sessionId = id(session);
		SparkClient client = (SparkClient) PushServiceHandler.pushService.createClient(session);
		if (!clientsMap.containsKey(sessionId)) clientsMap.put(sessionId, new ArrayList<>());
		clientsMap.get(sessionId).add(client);
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
		removeClientFromClientsMap(sessionId, client);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void removeClientFromClientsMap(String sessionId, SparkClient client) {
		if (!clientsMap.containsKey(sessionId)) return;
		List<SparkClient> clientList = clientsMap.get(sessionId);
		SparkClient savedClient = client != null ? clientList.stream().filter(c -> c.id().equals(client.id())).findFirst().orElse(null) : null;
		if (savedClient != null) clientsMap.get(sessionId).remove(savedClient);
		if (clientsMap.get(sessionId).size() <= 0) clientsMap.remove(sessionId);
	}

	private void refreshSession(Session session) {
		SparkClient client = registerClient(session);
		client.session(session);
		pushService.linkToThread(client);
	}

	private String clientId(Session session) {
		String[] params = ((WebSocketSession) session).getRequestURI().getQuery().split("&");
		if (params.length <= 0) return null;
		String[] split = params[0].split("=");
		return split.length > 1 ? split[1] : null;
	}

}

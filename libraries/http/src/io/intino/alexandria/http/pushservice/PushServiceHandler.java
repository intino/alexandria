package io.intino.alexandria.http.pushservice;

import io.intino.alexandria.http.PushListener;
import io.intino.alexandria.http.server.AlexandriaHttpClient;
import io.intino.alexandria.logger.Logger;
import io.javalin.websocket.WsConfig;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.time.Duration;
import java.util.*;

@WebSocket(maxTextMessageSize = 5 * 1024 * 1024)
public class PushServiceHandler implements PushListener {
	private final PushService pushService;
	private final Map<String, List<AlexandriaHttpClient>> clientsMap = new HashMap<>();
	private final Map<String, Timer> closeTimersMap = new HashMap<>();
	private static final int CloseTimeout = 1000 * 60 * 60 * 24;
	private static final int CloseGoingAway = 1001;
	private static final int CloseReadEOF = 1006;

	public PushServiceHandler(WsConfig socket, io.intino.alexandria.http.pushservice.PushService<?, ?> pushService) {
		this.pushService = (PushService<?, ?>) pushService;
		init(socket);
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		Client client = client(session);
		if (client != null) {
			cancelClose(session);
			client(session).session(session);
		} else registerClient(session);
		pushService.onOpen(client(session));
	}

	@OnWebSocketError
	public void onError(Session session, Throwable error) {
		String sessionId = AlexandriaHttpClient.sessionId(session);
		try {
			if (closeTimersMap.containsKey(sessionId)) {
				closeTimersMap.get(sessionId).cancel();
				closeTimersMap.remove(sessionId);
			}
		} catch (Throwable ex) {
			Logger.error(ex);
		}
		if (error instanceof EofException) return;
		if (error.getMessage() != null) Logger.debug(error.getMessage());
		else Logger.debug(error.toString());
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		String sessionId = AlexandriaHttpClient.sessionId(session);
		cancelClose(session);
		if (statusCode == CloseGoingAway) {
			//Logger.debug(String.format("WebSocket connection lost. Status code: %d. %s", statusCode, reason));
			doClose(sessionId, client(session));
			return;
		}
		doCloseDelayed(session, sessionId);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		Client client = client(session);
		if (client == null) session.disconnect();
		pushService.onMessage(client(session), message);
	}

	protected AlexandriaHttpClient client(Session session) {
		String sessionId = id(session);
		if (!clientsMap.containsKey(sessionId)) return null;
		String clientId = clientId(session);
		return clientsMap.get(sessionId).stream().filter(c -> c.id().equals(clientId)).findFirst().orElse(null);
	}

	protected AlexandriaHttpClient registerClient(Session session) {
		String sessionId = id(session);
		AlexandriaHttpClient client = (AlexandriaHttpClient) pushService.createClient(session);
		if (!clientsMap.containsKey(sessionId)) clientsMap.put(sessionId, new ArrayList<>());
		clientsMap.get(sessionId).add(client);
		return client;
	}

	private String id(Session session) {
		return AlexandriaHttpClient.sessionId(session);
	}

	private void cancelClose(Session session) {
		String sessionId = id(session);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void doCloseDelayed(Session session, String sessionId) {
		AlexandriaHttpClient client = client(session);
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

	private void doClose(String sessionId, AlexandriaHttpClient client) {
		if (client != null) pushService.onClose(client);
		removeClientFromClientsMap(sessionId, client);
		if (!closeTimersMap.containsKey(sessionId)) return;
		closeTimersMap.get(sessionId).cancel();
		closeTimersMap.remove(sessionId);
	}

	private void removeClientFromClientsMap(String sessionId, AlexandriaHttpClient client) {
		if (!clientsMap.containsKey(sessionId)) return;
		List<AlexandriaHttpClient> clientList = clientsMap.get(sessionId);
		AlexandriaHttpClient savedClient = client != null ? clientList.stream().filter(c -> c.id().equals(client.id())).findFirst().orElse(null) : null;
		if (savedClient != null) clientsMap.get(sessionId).remove(savedClient);
		if (clientsMap.containsKey(sessionId) && clientsMap.get(sessionId).isEmpty()) clientsMap.remove(sessionId);
	}

	private void refreshSession(Session session) {
		AlexandriaHttpClient client = registerClient(session);
		client.session(session);
		pushService.linkToThread(client);
	}

	private String clientId(Session session) {
		String[] params = ((WebSocketSession) session).getCoreSession().getRequestURI().getQuery().split("&");
		if (params.length <= 0) return null;
		String[] split = params[0].split("=");
		return split.length > 1 ? split[1] : null;
	}

	private static final int MaxTextMessageSize = 5 * 1024 * 1024; // 5 MB
	private static final int MaxIdleTimeout = 24 * 60 * 60 * 1000; // One day
	private void init(WsConfig socket) {
		socket.onConnect(context -> {
			context.session.getPolicy().setIdleTimeout(Duration.ofMillis(MaxIdleTimeout));
			context.session.getPolicy().setMaxTextMessageSize(MaxTextMessageSize);
			onConnect(context.session);
		});
		socket.onError(context -> onError(context.session, context.error()));
		socket.onClose(context -> onClose(context.session, context.status(), context.reason()));
		socket.onMessage(context -> onMessage(context.session, context.message()));
	}

}

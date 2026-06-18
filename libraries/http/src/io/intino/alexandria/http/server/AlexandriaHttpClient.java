package io.intino.alexandria.http.server;

import io.intino.alexandria.http.pushservice.Client;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class AlexandriaHttpClient implements Client {
	private Session session;
	private final Map<String, String> queryString;
	private String language = null;
	private Integer timezoneOffset = null;
	private final List<String> messagesQueue = new ArrayList<>();
	private Timer queueTimer;

	public AlexandriaHttpClient(Session session) {
		this.session = session;
		this.queryString = parseQueryString(session.getUpgradeRequest().getRequestURI().getQuery());
		this.runQueueManager();
	}

	public Session session() {
		return session;
	}

	public AlexandriaHttpClient session(Session session) {
		this.session = session;
		return this;
	}

	public static String sessionId(Session session) {
		Map<String, String> queryString = parseQueryString(session.getUpgradeRequest().getRequestURI().getQuery());
		return queryString.get("currentSession");
	}

	@Override
	public String id() {
		return queryString.get("id");
	}

	@Override
	public String sessionId() {
		return sessionId(session);
	}

	@Override
	public String language() {
		return this.language != null ? this.language : queryString.get("language");
	}

	@Override
	public void language(String language) {
		this.language = language;
	}

	@Override
	public int timezoneOffset() {
		return this.timezoneOffset != null ? this.timezoneOffset : (queryString.containsKey("tzo") ? Integer.parseInt(queryString.get("tzo")) : 0);
	}

	public void timezoneOffset(int value) {
		this.timezoneOffset = value;
	}

	@Override
	public boolean send(String message) {
		if (!session.isOpen()) return false;

		session.sendText(message, Callback.from(() -> {
			if (messagesQueue.contains(message)) messagesQueue.remove(message);
		}, throwable -> {
			if (!messagesQueue.contains(message)) messagesQueue.add(message);
		}));

		return true;
	}

	@Override
	public void destroy() {
		if (queueTimer != null) queueTimer.cancel();
		messagesQueue.clear();
	}

	@Override
	public int hashCode() {
		return sessionId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof AlexandriaHttpClient && id().equals(((AlexandriaHttpClient) obj).id());
	}

	private static Map<String, String> parseQueryString(String queryString) {
		if (queryString == null || queryString.isBlank()) return Collections.emptyMap();
		return Stream.of(queryString.split("&"))
				.map(param -> param.split("="))
				.collect(toMap(p -> p[0], p -> p.length > 1 ? p[1] : "", (a, b) -> a));
	}

	private void runQueueManager() {
		queueTimer = new Timer("alexandria http queue timer");
		queueTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!session.isOpen()) messagesQueue.clear();
				messagesQueue.forEach(message -> send(message));
			}
		}, 1000, 1000);
	}
}

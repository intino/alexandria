package io.intino.alexandria.foundation.spark;

import io.intino.alexandria.foundation.pushservice.Client;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class SparkClient implements Client {
	private final Session session;
	private final Map<String, String> queryString;
	private String language = null;
	private List<String> messagesQueue = new ArrayList<>();
	private Timer queueTimer;

	public SparkClient(Session session) {
		this.session = session;
		this.queryString = parseQueryString(session.getUpgradeRequest().getRequestURI().getQuery());
		this.runQueueManager();
	}

	@Override
	public String id() {
		return queryString.get("id");
	}

	@Override
	public String sessionId() {
		return queryString.get("currentSession");
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
	public void send(String message) {
		if (!session.isOpen()) return;

		session.getRemote().sendString(message, new WriteCallback() {
			@Override
			public void writeFailed(Throwable throwable) {
				if (!messagesQueue.contains(message))
					messagesQueue.add(message);
			}

			@Override
			public void writeSuccess() {
				if (messagesQueue.contains(message))
					messagesQueue.remove(message);
			}
		});
	}

	@Override
	public void destroy() {
		queueTimer.cancel();
		messagesQueue.clear();
	}

	@Override
	public int hashCode() {
		return sessionId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SparkClient && id().equals(((SparkClient) obj).id());
	}

	private Map<String, String> parseQueryString(String queryString) {
		return Stream.of(queryString.split("&"))
				.map(param -> param.split("="))
				.collect(toMap(p -> p[0], p -> p[1]));
	}

	private void runQueueManager() {
		queueTimer = new Timer();

		queueTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!session.isOpen()) messagesQueue.clear();
				messagesQueue.forEach(message -> send(message));
			}
		}, 1000, 1000);
	}
}

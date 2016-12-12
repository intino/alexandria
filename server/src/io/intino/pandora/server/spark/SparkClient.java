package io.intino.pandora.server.spark;

import org.eclipse.jetty.websocket.api.Session;
import io.intino.pandora.server.pushservice.Client;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class SparkClient implements Client {
	private final Session session;
	private final Map<String, String> queryString;
	private String language = null;

	public SparkClient(Session session) {
		this.session = session;
		this.queryString = parseQueryString(session.getUpgradeRequest().getRequestURI().getQuery());
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
		try {
			if (session.isOpen())
				session.getRemote().sendString(message);
		} catch (IOException e) {
			session.getRemote().sendStringByFuture(message);
		}
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
}

package org.siani.pandora.server.displays;

import org.siani.pandora.server.core.Client;
import org.siani.pandora.server.core.Session;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public interface MessageCarrier {

	void notifyAll(String message, Map<String, Object> parameters);

	void notify(Client client, String message, Map<String, Object> parameters);

	void notify(Session user, String message, Map<String, Object> parameters);

	default void notifyAll(String message) {
		notifyAll(message, emptyMap());
	}

	default void notifyAll(String message, Object parameterValue) {
		notifyAll(message, singletonMap(message, parameterValue));
	}

	default void notifyAll(String message, String parameter, Object parameterValue) {
		notifyAll(message, singletonMap(parameter, parameterValue));
	}

	default void notify(Session session, String message) {
		notify(session, message, emptyMap());
	}

	default void notify(Session session, String message, Object parameterValue) {
		notify(session, message, singletonMap(message, parameterValue));
	}

	default void notify(Session session, String message, String parameter, Object parameterValue) {
		notify(session, message, singletonMap(parameter, parameterValue));
	}

	default void notify(Client client, String message) {
		notify(client, message, emptyMap());
	}

	default void notify(Client client, String message, Object parameterValue) {
		notify(client, message, singletonMap(message, parameterValue));
	}

	default void notify(Client client, String message, String parameter, Object parameterValue) {
		notify(client, message, singletonMap(parameter, parameterValue));
	}

}

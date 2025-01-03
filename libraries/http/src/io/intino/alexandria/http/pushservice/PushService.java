package io.intino.alexandria.http.pushservice;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PushService<S extends Session<C>, C extends Client> {
	private final Map<String, List<Consumer<String>>> messageListeners = new HashMap<>();
	protected final Queue<Function<C, Boolean>> openConnectionListeners = new ConcurrentLinkedQueue<>();
	protected final Map<String, List<Consumer<C>>> closeConnectionListeners = new HashMap<>();
	protected final Map<String, List<Consumer<C>>> closeScheduledConnectionListeners = new HashMap<>();
	protected final SessionManager<S, C> sessionManager;

	public PushService() {
		this.sessionManager = new SessionManager<>();
	}

	public void onOpen(Function<C, Boolean> consumer) {
		openConnectionListeners.add(consumer);
	}

	public Connection onMessage(String clientId, Consumer<String> consumer) {
		messageListeners.putIfAbsent(clientId, new ArrayList<>());
		messageListeners.get(clientId).add(consumer);
		return () -> {
			synchronized (messageListeners) {
				if (!messageListeners.containsKey(clientId)) return;
				messageListeners.get(clientId).remove(consumer);
			}
		};
	}

	public ClosedConnection onClose(String clientId) {
		return consumer -> {
			closeConnectionListeners.putIfAbsent(clientId, new ArrayList<>());
			closeConnectionListeners.get(clientId).add(consumer);
		};
	}

	public ClosedConnection onCloseScheduled(String clientId) {
		return consumer -> {
			closeScheduledConnectionListeners.putIfAbsent(clientId, new ArrayList<>());
			closeScheduledConnectionListeners.get(clientId).add(consumer);
		};
	}

	public abstract S createSession(String id);

	public abstract C createClient(org.eclipse.jetty.websocket.api.Session session);

	public synchronized void onOpen(C client) {
		registerClient(client);
		sessionManager.linkToThread(client);

		List<Function<C, Boolean>> acceptListeners = new ArrayList<>();
		openConnectionListeners.forEach(listener -> {
			Boolean acceptClient = listener.apply(client);
			if (acceptClient) acceptListeners.add(listener);
		});
		acceptListeners.forEach(openConnectionListeners::remove);

		sessionManager.unlinkFromThread();
	}

	public void onMessage(C client, String message) {
		messageListeners.putIfAbsent(client.id(), new ArrayList<>());
		messageListeners.get(client.id()).forEach(listener -> listener.accept(message));
	}

	public void onClose(C client) {
		sessionManager.unRegister(client);
		messageListeners.remove(client.id());

		if (closeConnectionListeners.containsKey(client.id()))
			closeConnectionListeners.get(client.id()).forEach(clientConsumer -> clientConsumer.accept(client));
		closeConnectionListeners.remove(client.id());
	}

	public void onCloseScheduled(C client) {
		if (closeScheduledConnectionListeners.containsKey(client.id()))
			closeScheduledConnectionListeners.get(client.id()).forEach(clientConsumer -> clientConsumer.accept(client));
		closeScheduledConnectionListeners.remove(client.id());
	}

	public void pushBroadcast(String message) {
		sessionManager.sessions().forEach((Consumer<Session>) session ->
				session.clients().forEach((Consumer<Client>) client -> client.send(message)));
	}

	public void pushToSession(S session, String message) {
		session.send(message);
	}

	public void pushToClient(C client, String message) {
		client.send(message);
	}

	public void linkToThread(C client) {
		sessionManager.linkToThread(client);
	}

	public void unlinkFromThread() {
		sessionManager.unlinkFromThread();
	}

	public void unRegister(C client) {
		sessionManager.unRegister(client);
	}

	public boolean existsSession(String id) {
		return sessionManager.session(id) != null;
	}

	public S session(String id) {
		registerSession(id);
		return sessionManager.session(id);
	}

	public C client(String id) {
		return sessionManager.client(id);
	}

	public C currentClient() {
		return sessionManager.currentClient();
	}

	private void registerSession(String sessionId) {
		if (sessionManager.session(sessionId) != null) return;
		sessionManager.register(createSession(sessionId));
	}

	private void registerClient(C client) {
		registerSession(client.sessionId());
		sessionManager.register(client);
	}

	public interface OpenConnectionListener<C extends Client> {
		void onOpen(C client);
	}

	public interface MessageListener {
		void onMessage(String message);
	}

	public interface ClosedConnection<C extends Client> {
		void execute(Consumer<C> client);
	}

	public interface Connection {
		void unRegister();
	}

}

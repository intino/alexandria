package org.siani.pandora.server.spark;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.siani.pandora.server.pushservice.*;
import org.siani.pandora.server.ui.pushservice.DefaultRequestAdapter;
import org.siani.pandora.server.ui.pushservice.DefaultResponseAdapter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public abstract class PushService<S extends Session<C>, C extends Client> implements org.siani.pandora.server.pushservice.PushService<S, C> {

	protected final Queue<Consumer<C>> openConnectionListeners = new ConcurrentLinkedQueue<>();
	private final Map<String, List<Consumer<Message>>> messageListeners = new HashMap<>();
	protected final Map<String, List<Consumer<C>>> closeConnectionListeners = new HashMap<>();
	protected final AdapterProxy adapterProxy;
	protected final SessionManager<S, C> sessionManager;

	private static final JsonParser Parser = new JsonParser();

	public PushService() {
		this.adapterProxy = defaultAdapterProxy();
		this.sessionManager = new SessionManager<>();
	}

	@Override
	public void onOpen(Consumer<C> consumer) {
		openConnectionListeners.add(consumer);
	}

	@Override
	public Connection onMessage(String clientId, Consumer<Message> consumer) {
		messageListeners.putIfAbsent(clientId, new ArrayList<>());
		messageListeners.get(clientId).add(consumer);
		return () -> { synchronized(messageListeners) { messageListeners.get(clientId).remove(consumer); } };
	}

	@Override
	public ClosedConnection onClose(String clientId) {
		return consumer -> {
			closeConnectionListeners.putIfAbsent(clientId, new ArrayList<>());
			closeConnectionListeners.get(clientId).add(consumer);
		};
	}

	public abstract S createSession(String id);

	public abstract C createClient(org.eclipse.jetty.websocket.api.Session session);

	public void onOpen(C client) {
		registerClient(client);
		sessionManager.linkToThread(client);

		openConnectionListeners.forEach(listener -> listener.accept(client));
		openConnectionListeners.clear();

		sessionManager.unlinkFromThread();
	}

	public void onMessage(C client, String message) {
		broadcastMessage(client, new Message(message));
	}

	public void onClose(C client) {
		sessionManager.unRegister(client);
		messageListeners.remove(client.id());

		if (closeConnectionListeners.containsKey(client.id()))
			closeConnectionListeners.get(client.id()).forEach(clientConsumer -> clientConsumer.accept(client));

		closeConnectionListeners.remove(client.id());
	}

	@Override
	public void pushBroadcast(Message message) {
		sessionManager.sessions().stream().forEach(new Consumer<Session>() {
			@Override
			public void accept(Session session) {
				session.clients().forEach(new Consumer<Client>() {
					@Override
					public void accept(Client client) {
						client.send(PushService.this.serializeMessage(message));
					}
				});
			}
		});
	}

	@Override
	public void pushToSession(S session, Message message) {
		session.send(serializeMessage(message));
	}

	@Override
	public void pushToClient(C client, Message message) {
		client.send(serializeMessage(message));
	}

	@Override
	public void linkToThread(C client) {
		sessionManager.linkToThread(client);
	}

	@Override
	public void unlinkFromThread() {
		sessionManager.unlinkFromThread();
	}

	@Override
	public void unRegister(C client) {
		sessionManager.unRegister(client);
	}

	@Override
	public S session(String id) {
		registerSession(id);
		return sessionManager.session(id);
	}

	@Override
	public C client(String id) {
		return sessionManager.client(id);
	}

	@Override
	public C currentClient() {
		return sessionManager.currentClient();
	}

	private void broadcastMessage(C client, Message message) {
		messageListeners.get(client.id()).forEach(listener -> listener.accept(message));
	}

	private String serializeMessage(Message message) {
        JsonObject result = new JsonObject();
		Map<String, Object> parameters = message.parameters();

        result.addProperty("name", message.name());
		result.add("parameters", serializeMessageParameters(parameters));

        return result.toString();
    }

    private JsonElement serializeMessageParameters(Map<String, Object> parameters) {
        JsonObject result = new JsonObject();
        parameters.entrySet().stream().forEach(e -> {
            result.add(e.getKey(), serializeMessageParameter(e.getKey(), e.getValue()));
        });
        return result;
    }

    private JsonElement serializeMessageParameter(String key, Object value) {
        ResponseAdapter adapter = adapterProxy.responseAdapterOf(key);
        String result = value instanceof List ? adapter.adaptList((List) value) : adapter.adapt(value);

        try {
            return Parser.parse(result);
        } catch (Exception exception) {
            return new JsonPrimitive(result);
        }
    }

	private AdapterProxy defaultAdapterProxy() {
		return new AdapterProxy() {
			@Override
			public RequestAdapter requestAdapterOf(String name, Class clazz) {
				return new DefaultRequestAdapter(clazz);
			}

			@Override
			public ResponseAdapter responseAdapterOf(String name) {
				return new DefaultResponseAdapter();
			}
		};
	}

	private void registerSession(String sessionId) {
		if (sessionManager.session(sessionId) != null) return;
		sessionManager.register(createSession(sessionId));
	}

	private void registerClient(C client) {
		registerSession(client.sessionId());
		sessionManager.register(client);
	}

}

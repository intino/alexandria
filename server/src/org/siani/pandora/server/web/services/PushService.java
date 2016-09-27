package org.siani.pandora.server.web.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.siani.pandora.server.actions.AdapterProxy;
import org.siani.pandora.server.actions.ResponseAdapter;
import org.siani.pandora.server.core.Client;
import org.siani.pandora.server.core.Message;
import org.siani.pandora.server.core.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PushService implements org.siani.pandora.server.services.PushService {

	protected final List<Consumer<Client>> openConnectionListeners = new ArrayList<>();
	private final Map<String, List<Consumer<Message>>> messageListeners = new HashMap<>();
	protected final Map<String, List<Consumer<Client>>> closeConnectionListeners = new HashMap<>();
	protected final List<Client> clients = new ArrayList<>();
	protected final AdapterProxy adapterProxy;
	protected final BrowserService service;

	private static final JsonParser Parser = new JsonParser();
	private static final String Url = "?id=%s&session=%s&language=%s";

	public PushService(AdapterProxy adapterProxy, BrowserService service) {
		this.adapterProxy = adapterProxy;
		this.service = service;
	}

	@Override
	public String queryString(String clientId, String sessionId, String language) {
		return String.format(Url, clientId, sessionId, language);
	}

	@Override
	public void onOpen(Consumer<Client> consumer) {
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

	public void onOpen(Client client) {
		service.registerClient(client);
		service.linkToThread(client);

		clients.add(client);
		openConnectionListeners.forEach(listener -> listener.accept(client));
		openConnectionListeners.clear();

		service.unlinkFromThread();
	}

	public void onMessage(Client client, String message) {
		broadcastMessage(client, new Message(message));
	}

	public void onClose(Client client) {
		service.unRegisterClient(client);
		clients.remove(client);
		messageListeners.remove(client.id());

		if (closeConnectionListeners.containsKey(client.id()))
			closeConnectionListeners.get(client.id()).forEach(clientConsumer -> clientConsumer.accept(client));

		closeConnectionListeners.remove(client.id());
	}

	@Override
	public void pushBroadcast(Message message) {
		clients.stream().forEach(connection -> connection.send(serializeMessage(message)));
	}

	@Override
	public void pushToSession(Session session, Message message) {
		session.send(serializeMessage(message));
	}

	@Override
	public void pushToClient(Client client, Message message) {
		client.send(serializeMessage(message));
	}

	private void broadcastMessage(Client client, Message message) {
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

}

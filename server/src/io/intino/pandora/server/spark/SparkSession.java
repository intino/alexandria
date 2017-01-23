package io.intino.pandora.server.spark;

import io.intino.pandora.server.pushservice.Client;
import io.intino.pandora.server.pushservice.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SparkSession<C extends Client> implements Session<C> {
	private final Map<String, C> clientsMap = new HashMap<>();
	private final String id;
	private String currentClient;
	private Consumer<Boolean> loginListener;
	private Consumer<Boolean> logoutListener;

	public SparkSession(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}

	public List<C> clients() {
		return (List<C>) clientsMap.values();
	}

	@Override
	public C client(String id) {
		return clientsMap.get(id);
	}

	@Override
	public C currentClient() {
		return clientsMap.get(currentClient);
	}

	@Override
	public void currentClient(C client) {
		this.currentClient = client != null ? client.id() : null;
	}

	public void add(C client) {
		clientsMap.put(client.id(), client);
	}

	public void remove(C client) {
		if (!clientsMap.containsKey(client.id())) return;
		clientsMap.remove(client.id());
		this.currentClient = null;
	}

	public void send(String message) {
		clientsMap.values().stream().forEach(client -> client.send(message));
	}

	public void whenLogin(Consumer<Boolean> listener) {
		this.loginListener = listener;
	}

	public void whenLogout(Consumer<Boolean> listener) {
		this.logoutListener = listener;
	}

	@Override
	public void login() {
		if (loginListener != null)
			loginListener.accept(true);
	}

	@Override
	public void logout() {
		if (logoutListener != null)
			logoutListener.accept(true);
	}
}

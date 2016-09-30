package org.siani.pandora.server.spark;

import org.siani.pandora.server.pushservice.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparkSession<C extends Client> implements org.siani.pandora.server.pushservice.Session<C> {
	private final Map<String, C> clientsMap = new HashMap<>();
	private final String id;
	private String currentClient;

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
}

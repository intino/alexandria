package org.siani.pandora.server.core;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private final List<Client> clients = new ArrayList<>();
    private final String id;

    public Session(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public List<Client> clients() {
        return clients;
    }

    public void add(Client client) {
        clients.add(client);
    }

    public void remove(Client client) {
        clients.remove(client);
    }

    public void send(String message) {
        clients.stream().forEach(client -> client.send(message));
    }
}

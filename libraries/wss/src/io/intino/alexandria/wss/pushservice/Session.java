package io.intino.alexandria.wss.pushservice;

import io.intino.alexandria.wss.Client;

import java.util.List;

public interface Session<C extends Client> {
    String id();

    List<C> clients();
    C client(String id);
    C client();
    void clientProvider(ClientProvider<C> clientProvider);

    void send(String message);

    String login(String baseUrl);
    void logout();
}

package io.intino.konos.alexandria.rest.pushservice;

import java.util.List;

public interface Session<C extends Client> {
    String id();

    List<C> clients();
    C client(String id);
    C client();
    void currentClient(C client);

    void add(C client);
    void remove(C client);
    void send(String message);

    String login(String baseUrl);
    void logout();
}

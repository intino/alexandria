package io.intino.pandora.server.pushservice;

import java.util.List;

public interface Session<C extends Client> {
    String id();

    List<C> clients();
    C client(String id);
    C currentClient();
    void currentClient(C client);

    void add(C client);
    void remove(C client);
    void send(String message);

    void logout();
}

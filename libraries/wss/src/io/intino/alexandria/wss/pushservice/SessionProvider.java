package io.intino.alexandria.wss.pushservice;

import io.intino.alexandria.wss.Client;

public interface SessionProvider<S extends Session<C>, C extends Client> {
    boolean existsSession(String id);
    S session(String id);
    C client(String id);
    C currentClient();

    void linkToThread(C client);
    void unlinkFromThread();
    void unRegister(C client);
}

package io.intino.konos.server.pushservice;

public interface SessionProvider<S extends Session<C>, C extends Client> {
    S session(String id);
    C client(String id);
    C currentClient();

    void linkToThread(C client);
    void unlinkFromThread();
    void unRegister(C client);
}

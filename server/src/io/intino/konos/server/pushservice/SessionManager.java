package io.intino.konos.server.pushservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SessionManager<S extends Session<C>, C extends Client> {
    private final Map<String, S> sessionMap = new HashMap<>();
    private final Map<String, C> clientMap = new HashMap<>();
    private final Map<Long, S> linkSessionMap = new HashMap<>();
    private final Map<Long, C> linkClientMap = new HashMap<>();

    public List<S> sessions() {
        return sessionMap.values().stream().collect(Collectors.toList());
    }

    public S session(String id) {
        return sessionMap.containsKey(id) ? sessionMap.get(id) : null;
    }

    public void register(S session) {
        sessionMap.putIfAbsent(session.id(), session);
    }

    public void unRegister(S session) {
        session.clients().forEach(client -> unRegister(client));
        sessionMap.remove(session.id());
    }

    public C client(String id) {
        return clientMap.get(id);
    }

    public C client(String sessionId, String id) {
        return session(sessionId).client(id);
    }

    public C currentClient() {
        return linkClientMap.get(Thread.currentThread().getId());
    }

    public void register(C client) {
        clientMap.put(client.id(), client);
        session(client.sessionId()).add(client);
    }

    public void unRegister(C client) {
        client.destroy();
        clientMap.remove(client.id());
        session(client.sessionId()).remove(client);
    }

    public void linkToThread(C client) {
        long id = Thread.currentThread().getId();
        S session = session(client.sessionId());
        session.currentClient(client);
        linkSessionMap.put(id, session);
        linkClientMap.put(id, client);
    }

    public void unlinkFromThread() {
        long id = Thread.currentThread().getId();
        S session = linkSessionMap.get(id);
        if (session != null) session.currentClient(null);
        linkSessionMap.remove(id);
        linkClientMap.remove(id);
    }

}

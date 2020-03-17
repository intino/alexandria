package io.intino.alexandria.rest.pushservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SessionManager<S extends Session<C>, C extends Client> implements ClientProvider<C> {
    private final Map<String, S> sessionMap = new HashMap<>();
    private final Map<String, C> clientMap = new HashMap<>();
    private final Map<Long, C> linkClientMap = new HashMap<>();

    public List<S> sessions() {
        return new ArrayList<>(sessionMap.values());
    }

    public S session(String id) {
        return sessionMap.getOrDefault(id, null);
    }

    public void register(S session) {
        session.clientProvider(this);
        sessionMap.putIfAbsent(session.id(), session);
    }

    public void unRegister(S session) {
        session.clients().forEach(this::unRegister);
        sessionMap.remove(session.id());
    }

    @Override
    public List<C> clients(String sessionId) {
        return clientMap.values().stream().filter(c -> c.sessionId().equals(sessionId)).collect(toList());
    }

    public C client(String id) {
        return clientMap.get(id);
    }

    public C client() {
        return linkClientMap.get(Thread.currentThread().getId());
    }

    public C client(String sessionId, String id) {
        return session(sessionId).client(id);
    }

    public C currentClient() {
        return linkClientMap.get(Thread.currentThread().getId());
    }

    public void register(C client) {
        clientMap.put(client.id(), client);
    }

    public void unRegister(C client) {
        String sessionId = client.sessionId();
        client.destroy();
        clientMap.remove(client.id());
        if (sessionMap.get(sessionId) != null && sessionMap.get(sessionId).clients().size() <= 0) unRegister(sessionMap.get(sessionId));
    }

    public void linkToThread(C client) {
        long id = Thread.currentThread().getId();
        linkClientMap.put(id, client);
    }

    public void unlinkFromThread() {
        long id = Thread.currentThread().getId();
        linkClientMap.remove(id);
    }

}

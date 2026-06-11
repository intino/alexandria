package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.http.server.AlexandriaHttpClient;
import io.intino.alexandria.ui.Soul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIClient<S extends Soul> extends AlexandriaHttpClient {
    private S soul;
    private Map<String, String> cookies;
    private final Object pendingLock = new Object();
    private final List<String> pendingMessages = new ArrayList<>();
    private boolean ready = false;

    public UIClient(org.eclipse.jetty.websocket.api.Session session) {
        super(session);
    }

    public S soul() {
        return this.soul;
    }

    public void soul(S soul) {
        this.soul = soul;
        this.soul.personify();
    }

    public void cookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String cookie(String name) {
        return cookies.getOrDefault(name, null);
    }

    @Override
    public boolean send(String message) {
        synchronized (pendingLock) {
            if (!ready || !session().isOpen()) {
                pendingMessages.add(message);
                return false;
            }
        }
        return super.send(message);
    }

    public void ready() {
        synchronized (pendingLock) {
            if (ready) return;
            ready = true;
            flushPendingMessagesLocked();
        }
    }

    @Override
    public void destroy() {
        synchronized (pendingLock) {
            pendingMessages.clear();
        }
        super.destroy();
    }

    private void flushPendingMessagesLocked() {
        if (!ready || !session().isOpen()) return;
        if (pendingMessages.isEmpty()) return;
        List<String> messages = new ArrayList<>(pendingMessages);
        pendingMessages.clear();
        messages.forEach(UIClient.super::send);
    }
}

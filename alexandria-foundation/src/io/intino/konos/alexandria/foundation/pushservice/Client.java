package io.intino.konos.alexandria.foundation.pushservice;

public interface Client {
    String id();
    String sessionId();
    String language();
    void language(String language);
    void send(String message);
    void destroy();
}

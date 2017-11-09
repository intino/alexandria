package io.intino.konos.alexandria.rest.pushservice;

public interface Client {
    String id();
    String sessionId();
    String language();
    void language(String language);
    void send(String message);
    void destroy();
}

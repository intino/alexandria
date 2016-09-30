package org.siani.pandora.server.pushservice;

public interface Client {
    String id();
    String sessionId();
    String language();
    void language(String language);
    void send(String message);
}

package io.intino.konos.server.activity.services.auth;

public interface Token {
    String id();

    static Token build(String id) {
        return () -> id;
    }
}

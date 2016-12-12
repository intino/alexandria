package io.intino.pandora.server.activity.services.auth;

public interface Token {
    String id();

    static Token build(String id) {
        return () -> id;
    }
}

package io.intino.konos.alexandria.activity.services.auth;

public interface Token {
    String id();

    static Token build(String id) {
        return () -> id;
    }
}

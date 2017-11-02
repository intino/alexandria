package io.intino.konos.alexandria.foundation.activity.services.auth;

public interface Token {
    String id();

    static Token build(String id) {
        return () -> id;
    }
}

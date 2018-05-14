package io.intino.konos.alexandria.ui.services.auth;

public interface Token {
    String id();

    static Token build(String id) {
        return () -> id;
    }
}

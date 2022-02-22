package io.intino.alexandria.ui.services.auth;

public interface Token {
    String id();
    String secret();

    static Token build(String id) {
        return build(id, null);
    }

    static Token build(String id, String secret) {
        return new Token() {
            @Override
            public String id() {
                return id;
            }

            @Override
            public String secret() {
                return secret;
            }
        };
    }
}

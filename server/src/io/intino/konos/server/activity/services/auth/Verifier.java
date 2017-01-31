package io.intino.konos.server.activity.services.auth;

public interface Verifier {
    String value();

    static Verifier build(String value) {
        return () -> value;
    }
}

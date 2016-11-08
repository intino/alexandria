package io.intino.pandora.server.activity.services.auth;

public interface Verifier {
    String value();

    static Verifier build(String value) {
        return () -> value;
    }
}

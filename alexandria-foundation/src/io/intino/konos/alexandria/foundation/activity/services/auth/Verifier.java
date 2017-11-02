package io.intino.konos.alexandria.foundation.activity.services.auth;

public interface Verifier {
    String value();

    static Verifier build(String value) {
        return () -> value;
    }
}

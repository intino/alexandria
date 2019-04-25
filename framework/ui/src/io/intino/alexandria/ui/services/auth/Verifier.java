package io.intino.alexandria.ui.services.auth;

public interface Verifier {
    String value();

    static Verifier build(String value) {
        return () -> value;
    }
}

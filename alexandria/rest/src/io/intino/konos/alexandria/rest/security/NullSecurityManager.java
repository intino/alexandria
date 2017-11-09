package io.intino.konos.alexandria.rest.security;

public class NullSecurityManager implements AlexandriaSecurityManager {
    @Override
    public boolean check(String hash, String signature) {
        return true;
    }
}

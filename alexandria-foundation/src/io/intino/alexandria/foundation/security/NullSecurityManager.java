package io.intino.alexandria.foundation.security;

public class NullSecurityManager implements AlexandriaSecurityManager {
    @Override
    public boolean check(String hash, String signature) {
        return true;
    }
}

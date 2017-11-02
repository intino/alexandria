package io.intino.alexandria.foundation.security;

public interface AlexandriaSecurityManager {
    boolean check(String hash, String signature);
}

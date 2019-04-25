package io.intino.alexandria.rest.security;

public interface AlexandriaSecurityManager {
    boolean check(String hash, String signature);
}

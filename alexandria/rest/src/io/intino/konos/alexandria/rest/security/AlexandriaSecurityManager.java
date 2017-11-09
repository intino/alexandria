package io.intino.konos.alexandria.rest.security;

public interface AlexandriaSecurityManager {
    boolean check(String hash, String signature);
}

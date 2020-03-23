package io.intino.alexandria.http.security;

public interface AlexandriaSecurityManager {
    boolean check(String hash, String signature);
}

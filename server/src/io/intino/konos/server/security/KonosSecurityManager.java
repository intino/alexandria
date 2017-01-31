package io.intino.konos.server.security;

public interface KonosSecurityManager {
    boolean check(String hash, String signature);
}

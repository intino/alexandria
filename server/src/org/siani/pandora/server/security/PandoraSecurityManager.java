package org.siani.pandora.server.security;

public interface PandoraSecurityManager {
    boolean check(String hash, String signature);
}

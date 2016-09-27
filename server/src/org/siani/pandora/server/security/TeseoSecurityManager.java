package org.siani.pandora.server.security;

public interface TeseoSecurityManager {
    boolean check(String hash, String signature);
}

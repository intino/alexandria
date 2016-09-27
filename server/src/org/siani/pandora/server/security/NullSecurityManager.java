package org.siani.pandora.server.security;

import java.lang.*;

public class NullSecurityManager implements TeseoSecurityManager {
    @Override
    public boolean check(String hash, String signature) {
        return true;
    }
}

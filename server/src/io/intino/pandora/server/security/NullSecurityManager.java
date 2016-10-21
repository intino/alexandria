package io.intino.pandora.server.security;

import java.lang.*;

public class NullSecurityManager implements PandoraSecurityManager {
    @Override
    public boolean check(String hash, String signature) {
        return true;
    }
}

package io.intino.konos.server.security;

import java.lang.*;

public class NullSecurityManager implements KonosSecurityManager {
    @Override
    public boolean check(String hash, String signature) {
        return true;
    }
}

package teseo.framework.security;

import java.lang.*;

public class NullSecurityManager implements teseo.framework.security.SecurityManager {
    @Override
    public boolean check(String hash, String signature) {
        return true;
    }
}

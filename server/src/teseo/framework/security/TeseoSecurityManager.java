package teseo.framework.security;

public interface TeseoSecurityManager {
    boolean check(String hash, String signature);
}

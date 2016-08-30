package teseo.framework.security;

public interface SecurityManager {
    boolean check(String hash, String signature);
}

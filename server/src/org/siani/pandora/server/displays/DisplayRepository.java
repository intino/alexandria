package org.siani.pandora.server.displays;

public interface DisplayRepository {
    <T extends Display> T get(String id);
    <T extends Display> void register(T display);
    void remove(String id);
}

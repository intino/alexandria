package io.intino.pandora.server.activity.displays;

import java.util.function.Consumer;

public interface DisplayRepository {
    <T extends Display> T get(String id);
    <T extends Display> void register(T display);
    void addRegisterDisplayListener(Consumer<Display> consumer);
    void remove(String id);
}

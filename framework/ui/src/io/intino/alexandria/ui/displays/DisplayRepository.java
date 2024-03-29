package io.intino.alexandria.ui.displays;

import java.util.List;
import java.util.function.Consumer;

public interface DisplayRepository {
    List<Display> getAll();
    <T extends Display> T get(String id);
    <T extends Display> void register(T display);
    void addRegisterDisplayListener(Consumer<Display> consumer);
    <T extends Display> void remove(T display);
}

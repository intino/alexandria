package io.intino.konos.alexandria.activity.box.displays;

import java.util.List;
import java.util.function.Consumer;

public interface DisplayRepository {
    List<AlexandriaDisplay> getAll();
    <T extends AlexandriaDisplay> T get(String id);
    <T extends AlexandriaDisplay> void register(T display);
    void addRegisterDisplayListener(Consumer<AlexandriaDisplay> consumer);
    void remove(String id);
}

package io.intino.pandora.server.ui.displays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Soul implements DisplayRepository {
    private final Map<String, Display> displays = new HashMap();
    private final List<Consumer<Display>> registerListeners = new ArrayList<>();
    protected final ApplicationDisplay applicationDisplay;

    public Soul(ApplicationDisplay applicationDisplay) {
        this.applicationDisplay = applicationDisplay;
    }

    public <AD extends ApplicationDisplay> AD applicationDisplay() {
        return (AD) this.applicationDisplay;
    }

    public abstract void personify();

    @Override
    public <T extends Display> T get(String id) {
        return (T) this.displays.get(id);
    }

    @Override
    public <T extends Display> void register(T display) {
        this.displays.put(display.id(), display);
        registerListeners.forEach(listener -> listener.accept(display));
    }

    @Override
    public void addRegisterDisplayListener(Consumer<Display> consumer) {
        registerListeners.add(consumer);
    }

    @Override
    public void remove(String id) {
        this.displays.remove(id);
    }
}

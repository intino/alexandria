package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Soul implements DisplayRepository {
    private final Map<String, AlexandriaDisplay> displays = new HashMap();
    private final List<Consumer<AlexandriaDisplay>> registerListeners = new ArrayList<>();
    protected final ActivitySession session;
    protected User user;

    public Soul(ActivitySession session) {
        this.session = session;
    }

    public void user(User user) {
        this.user = user;
    }

    public abstract void personify();

    public URL baseAssetUrl() {
        try {
            return new URL(session.browser().baseAssetUrl());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public <T extends AlexandriaDesktop> T desktop() {
        return (T) displays.values().stream().filter(d -> d instanceof AlexandriaDesktop).findFirst().orElse(null);
    }

    @Override
    public List<AlexandriaDisplay> getAll() {
        return new ArrayList<>(this.displays.values());
    }

    @Override
    public <T extends AlexandriaDisplay> T get(String id) {
        return (T) this.displays.get(id);
    }

    @Override
    public <T extends AlexandriaDisplay> void register(T display) {
        this.displays.put(display.id(), display);
        registerListeners.forEach(listener -> listener.accept(display));
    }

    @Override
    public void addRegisterDisplayListener(Consumer<AlexandriaDisplay> consumer) {
        registerListeners.add(consumer);
    }

    @Override
    public void remove(String id) {
        this.displays.remove(id);
    }
}

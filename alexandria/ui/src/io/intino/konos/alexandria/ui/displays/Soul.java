package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.services.push.UISession;
import io.intino.konos.alexandria.ui.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public abstract class Soul implements DisplayRepository {
    private final Map<String, AlexandriaDisplay> displays = new HashMap<>();
    private final List<Consumer<AlexandriaDisplay>> registerListeners = new ArrayList<>();
    private Consumer<String> redirectListener = null;
    protected final UISession session;
    protected User user;

    public Soul(UISession session) {
        this.session = session;
    }

    public abstract void personify();

    public UISession session() {
        return session;
    }

    public User user() {
        return session != null ? session.user() : null;
    }

    public void destroy() {
        displays.values().forEach(AlexandriaDisplay::die);
        displays.clear();
    }

    public void onRedirect(Consumer<String> listener) {
        this.redirectListener = listener;
    }

    public void redirect(String url) {
        if (this.redirectListener == null) return;
        this.redirectListener.accept(url);
    }

    public URL baseAssetUrl() {
        try {
            return new URL(session.browser().baseAssetUrl());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public <T extends AlexandriaDisplay> List<T> displays(Class<T> clazz) {
        return displays.values().stream().filter(c -> clazz.isAssignableFrom(c.getClass())).map(clazz::cast).collect(toList());
    }

    public <T extends AlexandriaDisplay> List<T> moldsWithTarget(String target) {
        return displays.values().stream().filter(d -> {
            if (!(d instanceof AlexandriaItem)) return false;
            Item item = ((AlexandriaItem) d).item();
            return item != null && item.id().equals(target);
        }).map(d -> (T)d).collect(toList());
    }

    public <T extends AlexandriaDisplay> T displayWithId(String id) {
        return displays.containsKey(id) ? (T) displays.get(id) : null;
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

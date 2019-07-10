package io.intino.alexandria.ui;

import io.intino.alexandria.ui.displays.Desktop;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.DisplayRepository;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public abstract class Soul implements DisplayRepository {
    private final Map<String, Display> displays = new HashMap<>();
    private final List<Consumer<Display>> registerListeners = new ArrayList<>();
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
        displays.values().forEach(Display::remove);
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

    public <T extends Display> List<T> displays(Class<T> clazz) {
        return displays.values().stream().filter(c -> clazz.isAssignableFrom(c.getClass())).map(clazz::cast).collect(toList());
    }

    public <T extends Display> T displayWithId(String owner, String context, String id) {
        Display display = findDisplay(context, id);
        if (display != null) return (T) display;
        String ownerId = owner != null && !owner.isEmpty() ? owner : "";
        String key = ownerId + id;
        if (!displays.containsKey(key)) key = ownerId + id;
        return displays.containsKey(key) ? (T) displays.get(key) : null;
    }

    public <T extends Desktop> T desktop() {
        return (T) displays.values().stream().filter(d -> d instanceof Desktop).findFirst().orElse(null);
    }

    @Override
    public List<Display> getAll() {
        return new ArrayList<>(this.displays.values());
    }

    @Override
    public <T extends Display> T get(String id) {
        return (T) this.displays.get(id);
    }

    @Override
    public <T extends Display> void register(T display) {
        String ownerId = display.owner() != null ? display.owner().id() : "";
        String context = display.owner() != null ? display.owner().path() : "";
        this.displays.put(ownerId + display.id(), display);
        this.displays.put(context + display.id(), display);
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

    private Display findDisplay(String context, String id) {
        String[] contextArray = context.split("\\.");
        for (int i=0; i<contextArray.length; i++) {
            String key = String.join(".", Arrays.copyOfRange(contextArray, i, contextArray.length)) + id;
            if (displays.containsKey(key)) return displays.get(key);
        }
        return null;
    }
}

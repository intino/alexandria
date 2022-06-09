package io.intino.alexandria.ui;

import io.intino.alexandria.ui.displays.Desktop;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.DisplayRepository;
import io.intino.alexandria.ui.displays.components.Layer;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static io.intino.alexandria.ui.utils.UUIDUtil.isUUID;
import static java.util.stream.Collectors.toList;

public abstract class Soul implements DisplayRepository {
    private final Map<String, Display> displays = new ConcurrentHashMap<>();
    private final List<Consumer<Display>> registerListeners = new ArrayList<>();
    private final List<Layer<?, ?>> layers = new ArrayList<>();
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
        new ArrayList<>(displays.values()).stream().filter(Objects::nonNull).forEach(Display::remove);
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

    @SuppressWarnings("unchecked")
    public <T extends Display<?, ?>> T currentLayer(Class<T> clazz) {
        int i=layers.size();
        while (i > 0) {
            i--;
            Layer<?, ?> layer = layers.get(i);
            if (layer != null && clazz.isAssignableFrom(layer.template().getClass())) return (T) layer.template();
        }
        return displays(clazz).stream().filter(d -> d.owner() == null || !Layer.class.isAssignableFrom(d.owner().getClass())).findFirst().orElse(null);
    }

    public void pushLayer(Layer<?, ?> layer) {
        layers.add(layer);
    }

    public void popLayer() {
        try {
            if (layers.size() <= 0) return;
            int index = layers.size() - 1;
            Layer<?, ?> layer = layers.get(index);
            layers.remove(index);
            remove(layer.template());
        }
        catch (Throwable ignored) {
        }
    }

    public <T extends Display<?, ?>> List<T> displays(Class<T> clazz) {
        return new ArrayList<>(displays.values()).stream().filter(c -> clazz.isAssignableFrom(c.getClass())).map(clazz::cast).collect(toList());
    }

    public <T extends Display> T display(Class<T> clazz) {
        List<T> displays = displays(clazz);
        return displays.size() > 0 ? displays.get(0) : null;
    }

    public <T extends Display> T displayWithId(String owner, String context, String id) {
        Display display = findDisplay(owner, context, id);
        if (display != null) return (T) display;
        String ownerId = owner != null && !owner.isEmpty() ? owner : "";
        String key = ownerId + id;
        if (!displays.containsKey(key)) key = id;
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
        if (isUUID(display.id())) this.displays.put(display.id(), display);
        registerListeners.forEach(listener -> listener.accept(display));
    }

    @Override
    public void addRegisterDisplayListener(Consumer<Display> consumer) {
        registerListeners.add(consumer);
    }

    public <T extends Display> void remove(T display) {
        unregister(display);
        display.remove();
    }

    public <T extends Display> void unregister(T display) {
        String ownerId = display.owner() != null ? display.owner().id() : "";
        String context = display.owner() != null ? display.owner().path() : "";
        this.displays.remove(ownerId + display.id());
        this.displays.remove(context + display.id());
        if (isUUID(display.id())) this.displays.remove(display.id());
    }

    private Display findDisplay(String owner, String context, String id) {
        List<String> contextList = context != null && !context.isEmpty() ? Arrays.asList(context.split("\\.")) : new ArrayList<>();
        List<Display> result = displays.entrySet().stream().filter(e -> {
            String key = e.getKey();
            return key.endsWith(owner + id) && (e.getValue().owner() == null || containsAll(contextList, e.getValue().owner().path()));
        }).map(Map.Entry::getValue).collect(toList());
        return result.size() > 0 ? result.get(0) : null;
    }

    private boolean containsAll(List<String> contextList, String owner) {
        List<String> ownerList = owner != null ? Arrays.asList(owner.split("\\.")) : Collections.emptyList();
        if (ownerList.size() > 0 && ownerList.size() < contextList.size()) return contextList.containsAll(ownerList);
        else return ownerList.containsAll(contextList);
    }
}

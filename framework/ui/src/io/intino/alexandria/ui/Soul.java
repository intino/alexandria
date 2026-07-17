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
    private final Map<String, Display> displayLookup = new ConcurrentHashMap<>();
    private final List<Consumer<Display>> registerListeners = new ArrayList<>();
    private final List<Layer<?, ?>> layers = new ArrayList<>();
    private Consumer<String> redirectListener = null;
    protected final UISession session;
    protected User user;

    private static final char KEY_SEPARATOR = '\u0000';

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
        displayLookup.clear();
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

    public List<Layer<?, ?>> layers() {
        return layers;
    }

    public Layer<?, ?> currentLayer() {
        return !layers.isEmpty() ? layers.get(layers.size()-1) : null;
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

    public Layer<?, ?> popLayer() {
        try {
            if (layers.size() == 0) return null;
            int index = layers.size() - 1;
            Layer<?, ?> layer = layers.get(index);
            layers.remove(index);
            remove(layer.template());
            return layer;
        }
        catch (Throwable ignored) {
            return null;
        }
    }

    public void removeLayer(Layer<?, ?> layer) {
        layers.remove(layer);
        remove(layer.template());
    }

    public <T extends Display<?, ?>> List<T> displays(Class<T> clazz) {
        return new ArrayList<>(displays.values()).stream().filter(c -> clazz.isAssignableFrom(c.getClass())).map(clazz::cast).collect(toList());
    }

    public <T extends Display> T display(Class<T> clazz) {
        List<T> displays = displays(clazz);
        return displays.size() > 0 ? displays.get(0) : null;
    }

    public <T extends Display> T displayWithId(String owner, String context, String id) {
        if (id == null) return null;
        Display display = lookupDisplay(owner, context, id);
        if (display != null) return (T) display;
        return findDisplay(owner, context, id);
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
        indexDisplay(ownerId, display.id(), display);
        indexDisplay(context, display.id(), display);
        if (isUUID(display.id())) {
            this.displays.put(display.id(), display);
            this.displayLookup.put(display.id(), display);
        }
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
        removeDisplay(ownerId, display.id());
        removeDisplay(context, display.id());
        if (isUUID(display.id())) {
            this.displays.remove(display.id());
            this.displayLookup.remove(display.id());
        }
    }

    private Display lookupDisplay(String owner, String context, String id) {
        if (id == null) return null;
        Display display = lookupBy(owner, id);
        if (matchesContext(display, context)) return display;
        display = lookupBy(context, id);
        if (matchesContext(display, context)) return display;
        if (isUUID(id)) {
            display = displays.get(id);
            if (matchesContext(display, context)) return display;
        }
        return null;
    }

    private <T extends Display> T findDisplay(String owner, String context, String id) {
        String ownerValue = owner != null ? owner : "";
        List<String> contextList = context != null && !context.isEmpty() ? Arrays.asList(context.split("\\.")) : new ArrayList<>();
        List<Display> result = displays.entrySet().stream().filter(e -> {
            String key = e.getKey();
            return key.endsWith(ownerValue + id) && (e.getValue().owner() == null || containsAll(contextList, e.getValue().owner().path()));
        }).map(Map.Entry::getValue).collect(toList());
        return result.size() > 0 ? (T) result.get(0) : null;
    }

    private boolean containsAll(List<String> contextList, String owner) {
        List<String> ownerList = owner != null ? Arrays.asList(owner.split("\\.")) : Collections.emptyList();
        if (ownerList.isEmpty() || contextList.isEmpty()) return ownerList.equals(contextList);
        if (ownerList.size() > contextList.size()) return hasSameTail(contextList, ownerList);
        return hasSameTail(contextList, ownerList);
    }

    private boolean hasSameTail(List<String> expected, List<String> actual) {
        if (expected.size() == 0 || expected.size() > actual.size()) return false;
        int offset = actual.size() - expected.size();
        for (int i = 0; i < expected.size(); i++) {
            if (!Objects.equals(expected.get(i), actual.get(i + offset))) return false;
        }
        return true;
    }

    private void indexDisplay(String prefix, String id, Display display) {
        this.displays.put(prefix + id, display);
        this.displayLookup.put(lookupKey(prefix, id), display);
    }

    private void removeDisplay(String prefix, String id) {
        this.displays.remove(prefix + id);
        this.displayLookup.remove(lookupKey(prefix, id));
    }

    private Display lookupBy(String prefix, String id) {
        Display display = this.displayLookup.get(lookupKey(prefix, id));
        if (display != null) return display;
        String legacyKey = (prefix != null ? prefix : "") + id;
        return this.displays.get(legacyKey);
    }

    private boolean matchesContext(Display display, String context) {
        if (display == null || display.owner() == null) return display != null;
        List<String> contextList = context != null && !context.isEmpty() ? Arrays.asList(context.split("\\.")) : Collections.emptyList();
        return containsAll(contextList, display.owner().path());
    }

    private String lookupKey(String prefix, String id) {
        return (prefix != null ? prefix : "") + KEY_SEPARATOR + id;
    }
}

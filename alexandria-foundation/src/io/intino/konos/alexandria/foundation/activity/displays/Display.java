package io.intino.konos.alexandria.foundation.activity.displays;

import io.intino.konos.alexandria.foundation.activity.Asset;
import io.intino.konos.alexandria.foundation.activity.services.push.ActivitySession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class Display<N extends DisplayNotifier> {
    private final String id;
    private final List<Display> children = new ArrayList<>();
    protected DisplayRepository repository;
    protected N notifier;
    private SoulProvider soulProvider;
    private ActivitySession session;

    public Display() {
        this.id = UUID.randomUUID().toString();
    }

    public void inject(ActivitySession session) {
        this.session = session;
    }

    public void inject(N notifier) {
        this.notifier = notifier;
    }

    public void inject(DisplayRepository repository) {
        this.repository = repository;
    }

    public void inject(SoulProvider soulProvider) {
        this.soulProvider = soulProvider;
    }

    public final void personify() {
        notifier.personify(id, name());
        init();
    }

    public final void personify(String object) {
        notifier.personify(id, name(), object);
        init();
    }

    public final void personifyOnce() {
        notifier.personifyOnce(id, name());
        init();
    }

    public final void personifyOnce(String object) {
        notifier.personifyOnce(id, name(), object);
        init();
    }

    public void setLanguage(String language) {
        propagateLanguageChanged(language);
    }

    public ActivitySession session() {
        return session;
    }

    protected void init() {
    }

    protected <S extends Soul> S soul() {
        return (S) soulProvider.soul();
    }

    protected String assetUrl(URL asset) {
        return Asset.toResource(soul().baseAssetUrl(), asset).toUrl().toString();
    }

    protected String assetUrl(URL asset, String label) {
        return Asset.toResource(soul().baseAssetUrl(), asset).setLabel(label).toUrl().toString();
    }

    private void propagateLanguageChanged(String language) {
        if (this instanceof International)
            ((International) this).onChangeLanguage(language);
        children.stream().forEach(c -> c.propagateLanguageChanged(language));
    }

    public void refresh() {
    }

    public String id() {
        return id;
    }

    public void die() {
        notifier.die(id);
    }

    public <T extends Display> List<T> children(Class<T> clazz) {
        return children.stream()
                .filter(child -> clazz.isAssignableFrom(child.getClass()))
                .map(clazz::cast)
                .collect(toList());
    }

    public <T extends Display> T child(Class<T> clazz) {
        return children(clazz).stream().findFirst().map(clazz::cast).orElse(null);
    }

    public void add(Display child) {
        repository.register(child);
        this.children.add(child);
    }

    public void addAndPersonify(Display child) {
        add(child);
        child.personify();
    }

    public void remove(Class<? extends Display> clazz) {
        List<? extends Display> childrenToRemove = children(clazz);
        childrenToRemove.stream().forEach(this::removeChild);
    }

    private void removeChild(Display display) {
        display.die();
        children.remove(display);
        repository.remove(display.id);
    }

    public String name() {
        return nameOf(this.getClass());
    }

    public static String nameOf(Class<? extends Display> clazz) {
        return clazz.getSimpleName().replace("Display", "").toLowerCase();
    }

}
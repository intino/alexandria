package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.Asset;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class AlexandriaDisplay<N extends AlexandriaDisplayNotifier> {
    private final String id;
    private final List<AlexandriaDisplay> children = new ArrayList<>();
    protected DisplayRepository repository;
    protected N notifier;
    private SoulProvider soulProvider;
    private ActivitySession session;

    public AlexandriaDisplay() {
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

    public <T extends AlexandriaDisplay> List<T> children(Class<T> clazz) {
        return children.stream()
                .filter(child -> clazz.isAssignableFrom(child.getClass()))
                .map(clazz::cast)
                .collect(toList());
    }

    public <T extends AlexandriaDisplay> T child(Class<T> clazz) {
        return children(clazz).stream().findFirst().map(clazz::cast).orElse(null);
    }

    public void add(AlexandriaDisplay child) {
        repository.register(child);
        this.children.add(child);
    }

    public void addAndPersonify(AlexandriaDisplay child) {
        add(child);
        child.personify();
    }

    public void remove(Class<? extends AlexandriaDisplay> clazz) {
        List<? extends AlexandriaDisplay> childrenToRemove = children(clazz);
        childrenToRemove.stream().forEach(this::removeChild);
    }

    private void removeChild(AlexandriaDisplay display) {
        display.die();
        children.remove(display);
        repository.remove(display.id);
    }

    public String name() {
        return nameOf(this.getClass());
    }

    public static String nameOf(Class<? extends AlexandriaDisplay> clazz) {
        String name = clazz.getSimpleName();
        int index = name.lastIndexOf("Display");
        return name.substring(0, index);
    }

}
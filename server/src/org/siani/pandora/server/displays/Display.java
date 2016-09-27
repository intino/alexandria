package org.siani.pandora.server.displays;

import org.siani.pandora.server.services.BrowserService;
import org.siani.pandora.server.services.ServiceSupplier;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;

public class Display {
    private final String id;
    private final List<Display> children = new ArrayList<>();
    protected final MessageCarrier carrier;
    protected final DisplayRepository repository;
    protected final ClientProvider clientProvider;
    protected ServiceSupplier serviceSupplier;

    public Display(MessageCarrier carrier, DisplayRepository repository, ClientProvider clientProvider) {
        this.id = UUID.randomUUID().toString();
        this.carrier = carrier;
        this.repository = repository;
        this.clientProvider = clientProvider;
    }

    public final void personify() {
        carry("personify", initializationParameters());
        init();
    }

    public final void personifyOnce() {
        carry("personifyOnce", initializationParameters());
        init();
    }

    protected void init() {
    }

    public void setLanguage(String language) {
        propagateLanguageChanged(language);
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
        carry("die", singletonMap("id", id));
    }

    public void inject(ServiceSupplier serviceSupplier) {
        this.serviceSupplier = serviceSupplier;
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
        child.inject(serviceSupplier);
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

    protected void broadcast(String message) {
        broadcast(message, emptyMap());
    }

    protected void broadcast(String message, Map<String, Object> parameters) {
        carrier.notifyAll(message, parameters);
    }

    protected void broadcastWithId(String message, Map<String, Object> parameters) {
        carrier.notifyAll(message, addIdTo(parameters));
    }

    protected void broadcastWithId(String message) {
        broadcastWithId(message, new HashMap<>());
    }

    protected void broadcast(String message, Object parameter) {
        carrier.notifyAll(message, parameter);
    }

    protected void broadcastWithId(String message, Object parameter) {
        carrier.notifyAll(message, addIdTo(singletonMap(message, parameter)));
    }

    protected void broadcast(String message, String parameter, Object value) {
        carrier.notifyAll(message, parameter, value);
    }

    protected void broadcastWithId(String message, String parameter, Object value) {
        carrier.notifyAll(message, addIdTo(singletonMap(parameter, value)));
    }

    protected void carry(String message) {
        carry(message, emptyMap());
    }

    protected void carry(String message, Map<String, Object> parameters) {
        carrier.notify(clientProvider.client(), message, parameters);
    }

    protected void carryWithId(String message, Map<String, Object> parameters) {
        carrier.notify(clientProvider.client(), message, addIdTo(parameters));
    }

    protected void carryWithId(String message) {
        carryWithId(message, new HashMap<>());
    }

    protected void carry(String message, Object parameter) {
        carrier.notify(clientProvider.client(), message, parameter);
    }

    protected void carryWithId(String message, Object parameter) {
        carrier.notify(clientProvider.client(), message, addIdTo(singletonMap(message, parameter)));
    }

    protected void carry(String message, String parameter, Object value) {
        carrier.notify(clientProvider.client(), message, parameter, value);
    }

    protected void carryWithId(String message, String parameter, Object value) {
        carrier.notify(clientProvider.client(), message, addIdTo(singletonMap(parameter, value)));
    }

    private BrowserService browserService() {
        return serviceSupplier.service(BrowserService.class);
    }

    private Map<String, Object> initializationParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("display", name());
        return parameters;
    }

    private Map<String, Object> addIdTo(Map<String, Object> parameters) {
        Map<String, Object> parametersWithId = new HashMap<>(parameters);
        parametersWithId.put("id", id);
        return parametersWithId;
    }

}
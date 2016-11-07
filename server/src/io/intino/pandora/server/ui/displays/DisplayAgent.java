package io.intino.pandora.server.ui.displays;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class DisplayAgent {
    Display display;
    MessageCarrier carrier;

    public DisplayAgent(Display display, MessageCarrier carrier) {
        this.display = display;
        this.carrier = carrier;
    }

    public void personify(String id, String name) {
        put("personify", initializationParameters(id, name));
    }

    public void personifyOnce(String id, String name) {
        put("personifyOnce", initializationParameters(id, name));
    }

    public void die(String id) {
        put("die", singletonMap("id", id));
    }

    protected void put(String message) {
        put(message, emptyMap());
    }

    protected void put(String message, Map<String, Object> parameters) {
        carrier.notifyClient(message, parameters);
    }

    protected void put(String message, Object parameter) {
        carrier.notifyClient(message, parameter);
    }

    protected void put(String message, String parameter, Object value) {
        carrier.notifyClient(message, parameter, value);
    }

    protected void putToOwner(String message, Map<String, Object> parameters) {
        carrier.notifyClient(message, addIdTo(parameters));
    }

    protected void putToOwner(String message) {
        putToOwner(message, new HashMap<>());
    }

    protected void putToOwner(String message, Object parameter) {
        carrier.notifyClient(message, addIdTo(singletonMap(message, parameter)));
    }

    protected void putToOwner(String message, String parameter, Object value) {
        carrier.notifyClient(message, addIdTo(singletonMap(parameter, value)));
    }

    protected void putToAll(String message) {
        putToAll(message, emptyMap());
    }

    protected void putToAll(String message, Map<String, Object> parameters) {
        carrier.notifyAll(message, parameters);
    }

    protected void putToAll(String message, Object parameter) {
        carrier.notifyAll(message, parameter);
    }

    protected void putToAll(String message, String parameter, Object value) {
        carrier.notifyAll(message, parameter, value);
    }

    private Map<String, Object> addIdTo(Map<String, Object> parameters) {
        HashMap parametersWithId = new HashMap(parameters);
        parametersWithId.put("id", display.id());
        return parametersWithId;
    }

    private Map<String, Object> initializationParameters(String id, String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("display", name);
        return parameters;
    }

}

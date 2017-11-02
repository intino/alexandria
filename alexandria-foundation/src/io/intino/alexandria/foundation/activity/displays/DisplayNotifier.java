package io.intino.alexandria.foundation.activity.displays;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class DisplayNotifier {
    Display display;
    MessageCarrier carrier;

    public DisplayNotifier(Display display, MessageCarrier carrier) {
        this.display = display;
        this.carrier = carrier;
    }

    public void personify(String id, String name) {
        put("personify", initializationParameters(id, name));
    }

    public void personify(String id, String name, String object) {
        put("personify", initializationParameters(id, name, object));
    }

    public void personifyOnce(String id, String name) {
        put("personifyOnce", initializationParameters(id, name));
    }

    public void personifyOnce(String id, String name, String object) {
        put("personifyOnce", initializationParameters(id, name, object));
    }

    public void die(String id) {
        put("die", singletonMap("id", id));
    }

    protected void put(String message) {
        put(message, addIdAndNameTo(new HashMap<>()));
    }

    protected void put(String message, Map<String, Object> parameters) {
        carrier.notifyClient(message, addIdAndNameTo(parameters));
    }

    protected void put(String message, Object parameter) {
        carrier.notifyClient(message, addIdAndNameTo(singletonMap(message, parameter)));
    }

    protected void put(String message, String parameter, Object value) {
        carrier.notifyClient(message, addIdAndNameTo(singletonMap(parameter, value)));
    }

    protected void putToDisplay(String message, Map<String, Object> parameters) {
        carrier.notifyClient(message, addIdAndNameTo(parameters));
    }

    protected void putToDisplay(String message) {
        putToDisplay(message, addIdAndNameTo(new HashMap<>()));
    }

    protected void putToDisplay(String message, Object parameter) {
        carrier.notifyClient(message, addIdAndNameTo(singletonMap(message, parameter)));
    }

    protected void putToDisplay(String message, String parameter, Object value) {
        carrier.notifyClient(message, addIdAndNameTo(singletonMap(parameter, value)));
    }

    protected void putToAll(String message) {
        putToAll(message, addIdAndNameTo(new HashMap<>()));
    }

    protected void putToAll(String message, Map<String, Object> parameters) {
        carrier.notifyAll(message, addIdAndNameTo(parameters));
    }

    protected void putToAll(String message, Object parameter) {
        carrier.notifyAll(message, addIdAndNameTo(singletonMap(message, parameter)));
    }

    protected void putToAll(String message, String parameter, Object value) {
        carrier.notifyAll(message, addIdAndNameTo(singletonMap(parameter, value)));
    }

    private Map<String, Object> addIdAndNameTo(Map<String, Object> parameters) {
        HashMap parametersWithId = new HashMap(parameters);
        parametersWithId.put("id", display.id());
        parametersWithId.put("name", display.name());
        return parametersWithId;
    }

    private Map<String, Object> initializationParameters(String id, String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("display", name);
        return parameters;
    }

    private Map<String, Object> initializationParameters(String id, String name, String object) {
        Map<String, Object> parameters = initializationParameters(id, name);
        parameters.put("object", object);
        return parameters;
    }

}

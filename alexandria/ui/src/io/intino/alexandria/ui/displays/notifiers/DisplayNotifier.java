package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.PropertyList;

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

    public void add(Display child, String container) {
        String type = child.getClass().getSimpleName();
        PropertyList propertyList = child.properties();
        put("addInstance", addMetadata(registerParameters(type, propertyList, container, -1)));
    }

    public void insert(Display child, int index, String container) {
        String type = child.getClass().getSimpleName();
        PropertyList propertyList = child.properties();
        put("insertInstance", addMetadata(registerParameters(type, propertyList, container, index)));
    }

    public void remove(String id, String container) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("c", container);
        put("removeInstance", addMetadata(params));
    }

    protected void put(String message) {
        put(message, addMetadata(new HashMap<>()));
    }

    protected void put(String message, Map<String, Object> parameters) {
        carrier.notifyClient(message, addMetadata(parameters));
    }

    protected void put(String message, Object parameter) {
        carrier.notifyClient(message, addMetadata(singletonMap(message, parameter)));
    }

    protected void put(String message, String parameter, Object value) {
        carrier.notifyClient(message, addMetadata(singletonMap(parameter, value)));
    }

    protected void putToDisplay(String message, Map<String, Object> parameters) {
        carrier.notifyClient(message, addMetadata(parameters));
    }

    protected void putToDisplay(String message) {
        putToDisplay(message, addMetadata(new HashMap<>()));
    }

    protected void putToDisplay(String message, Object parameter) {
        carrier.notifyClient(message, addMetadata(singletonMap(message, parameter)));
    }

    protected void putToDisplay(String message, String parameter, Object value) {
        carrier.notifyClient(message, addMetadata(singletonMap(parameter, value)));
    }

    protected void putToAll(String message) {
        putToAll(message, addMetadata(new HashMap<>()));
    }

    protected void putToAll(String message, Map<String, Object> parameters) {
        carrier.notifyAll(message, addMetadata(parameters));
    }

    protected void putToAll(String message, Object parameter) {
        carrier.notifyAll(message, addMetadata(singletonMap(message, parameter)));
    }

    protected void putToAll(String message, String parameter, Object value) {
        carrier.notifyAll(message, addMetadata(singletonMap(parameter, value)));
    }

    private Map<String, Object> addMetadata(Map<String, Object> parameters) {
        HashMap parametersWithId = new HashMap(parameters);
        parametersWithId.put("i", display.id());
        parametersWithId.put("n", display.name());
        if (display.owner() != null) parametersWithId.put("o", display.owner().path());
        return parametersWithId;
    }

    private Map<String, Object> registerParameters(String type, PropertyList propertyList, String container, int index) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tp", type);
        parameters.put("pl", propertyList);
        parameters.put("c", container);
        if (index != -1) parameters.put("idx", index);
        return parameters;
    }

}


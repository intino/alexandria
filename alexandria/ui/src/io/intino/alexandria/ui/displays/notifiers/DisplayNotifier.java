package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.PropertyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.reverse;
import static java.util.Collections.singletonMap;

public class DisplayNotifier {
    Display display;
    MessageCarrier carrier;

    public DisplayNotifier(Display display, MessageCarrier carrier) {
        this.display = display;
        this.carrier = carrier;
    }

    public void add(Display child) {
        String type = child.getClass().getSimpleName();
        PropertyList propertyList = child.properties();
        put("add", addMetadata(registerParameters(type, propertyList)));
    }

    public void remove(String id) {
        put("remove", singletonMap("id", id));
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
        parametersWithId.put("i", id(display));
        parametersWithId.put("n", display.name());
        if (display.owner() != null) parametersWithId.put("o", display.owner().id());
        return parametersWithId;
    }

    private String id(Display display) {
        Display owner = display.parent();
        List<String> result = new ArrayList<>();
        result.add(display.id());
        while (owner != null) {
            result.add(owner.id());
            owner = owner.parent();
        }
        reverse(result);
        return String.join(".", result);
    }

    private Map<String, Object> registerParameters(String type, PropertyList propertyList) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tp", type);
        parameters.put("pl", propertyList);
        return parameters;
    }

}


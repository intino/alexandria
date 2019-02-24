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

    public void add(Display child) {
        String type = child.getClass().getSimpleName();
        PropertyList propertyList = child.properties();
        put("addChild", addIdAndNameTo(registerParameters(type, propertyList)));
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
        parametersWithId.put("i", display.id());
        parametersWithId.put("n", display.name());
        return parametersWithId;
    }

    private Map<String, Object> registerParameters(String type, PropertyList propertyList) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tp", type);
        parameters.put("pl", propertyList);
        return parameters;
    }

}

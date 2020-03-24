package io.intino.alexandria.ui.displays.notifiers;

import io.intino.alexandria.Json;
import io.intino.alexandria.http.pushservice.MessageCarrier;
import io.intino.alexandria.ui.Message;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.PropertyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class DisplayNotifier {
    Display display;
    MessageCarrier carrier;

    public DisplayNotifier(Display display, MessageCarrier carrier) {
        this.display = display;
        this.carrier = carrier;
    }

    public <D extends Display> void add(D child, String container) {
        put("addInstance", addMetadata(registerParameters(child, container, -1)));
    }

    public <D extends Display> void add(List<D> children, String container) {
        Map<String, Object> params = new HashMap<>();
        params.put("c", container);
        params.put("value", registerParameters(children, -1, container));
        put("addInstances", addMetadata(params));
    }

    public <D extends Display> void insert(D child, int index, String container) {
        put("insertInstance", addMetadata(registerParameters(child, container, index)));
    }

    public <D extends Display> void insert(List<D> children, int index, String container) {
        Map<String, Object> params = new HashMap<>();
        params.put("c", container);
        params.put("value", registerParameters(children, index, container));
        put("insertInstances", addMetadata(params));
    }

    public void remove(String id, String container) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("c", container);
        put("removeInstance", addMetadata(params));
    }

	public void redirect() {
    	redirect(null);
	}

    public void redirect(String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        put("redirect", addMetadata(params));
    }

    public void clearContainer(String container) {
        Map<String, Object> params = new HashMap<>();
        params.put("c", container);
        put("clearContainer", addMetadata(params));
    }

    public void addressed(String address) {
        Map<String, Object> params = new HashMap<>();
        params.put("address", address);
        put("addressed", addMetadata(params));
    }

    protected void put(String message) {
        put(message, addMetadata(new HashMap<>()));
    }

    protected void put(String message, Map<String, Object> parameters) {
        carrier.notifyClient(Json.toString(new Message(message, addMetadata(parameters))));
    }

    protected void put(String message, Object parameter) {
        carrier.notifyClient(Json.toString(new Message(message, addMetadata(singletonMap(message, parameter)))));
    }

    protected void put(String message, String parameter, Object value) {
        carrier.notifyClient(Json.toString(new Message(message, addMetadata(singletonMap(parameter, value)))));
    }

    protected void putToDisplay(String message, Map<String, Object> parameters) {
        carrier.notifyClient(Json.toString(new Message(message, addMetadata(parameters))));
    }

    protected void putToDisplay(String message) {
        putToDisplay(message, addMetadata(new HashMap<>()));
    }

    protected void putToDisplay(String message, Object parameter) {
        carrier.notifyClient(Json.toString(new Message(message, addMetadata(singletonMap(message, parameter)))));
    }

    protected void putToDisplay(String message, String parameter, Object value) {
        carrier.notifyClient(Json.toString(new Message(message, addMetadata(singletonMap(parameter, value)))));
    }

    protected void putToAll(String message) {
        putToAll(message, addMetadata(new HashMap<>()));
    }

    protected void putToAll(String message, Map<String, Object> parameters) {
        carrier.notifyAll(Json.toString(new Message(message, addMetadata(parameters))));
    }

    protected void putToAll(String message, Object parameter) {
        carrier.notifyAll(Json.toString(new Message(message, addMetadata(singletonMap(message, parameter)))));
    }

    protected void putToAll(String message, String parameter, Object value) {
        carrier.notifyAll(Json.toString(new Message(message, addMetadata(singletonMap(parameter, value)))));
    }

    private Map<String, Object> addMetadata(Map<String, Object> parameters) {
        HashMap parametersWithId = new HashMap(parameters);
        parametersWithId.put("i", display.id());
        parametersWithId.put("n", display.name());
        if (display.owner() != null) parametersWithId.put("o", display.owner().path());
        return parametersWithId;
    }

    private <D extends Display> List<Map<String, Object>> registerParameters(List<D> children, int index, String container) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i=0; i<children.size(); i++) result.add(registerParameters(children.get(i), container, i+index));
        return result;
    }

    private <D extends Display> Map<String, Object> registerParameters(D display, String container, int index) {
        String type = display.getClass().getSimpleName();
        return registerParameters(type, display.properties(), container, index);
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


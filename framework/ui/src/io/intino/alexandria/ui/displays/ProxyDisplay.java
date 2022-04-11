package io.intino.alexandria.ui.displays;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.intino.alexandria.Json;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.restaccessor.core.RestAccessor;
import io.intino.alexandria.restaccessor.exceptions.RestfulFailure;
import io.intino.alexandria.schemas.ProxyDisplayInfo;
import io.intino.alexandria.ui.displays.notifiers.ProxyDisplayNotifier;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.pages.Unit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class ProxyDisplay<DN extends ProxyDisplayNotifier> extends Display<DN, Box> {
    private final String type;
    private final Unit unit;
    private final String path;
    private String sessionId;
    private String clientId;
    private String token;
    private boolean ready = false;
    private static final JsonParser Parser = new JsonParser();
    private Set<PendingRequest> pendingRequestList = new LinkedHashSet<>();
    private Map<String, String> parameters = new HashMap<>();

    public ProxyDisplay(String type, Unit unit, String path) {
        super(null);
        this.type = type;
        this.unit = unit;
        this.path = path;
    }

    public ProxyDisplay<DN> session(UISession session) {
        this.sessionId = session.id();
        this.clientId = session.client().id();
        this.token = session.token() != null ? session.token().id() : null;
        return this;
    }

    @Override
    public void refresh() {
        try {
            if (sessionId == null || clientId == null) {
                throw new RuntimeException("You must set session to start this component");
            }
            if (!ready) return;
            post("?operation=refreshPersonifiedDisplay", parameters());
        } catch (RestfulFailure | MalformedURLException error) {
            notifier.refreshError(errorMessage(unit.url()));
        }
    }

    public Map<String, String> parameters() {
        return parameters;
    }

    public ProxyDisplay parameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ProxyDisplay add(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    @Override
    public void init() {
        super.init();
        try {
            notifier.refresh(new ProxyDisplayInfo().unit(unit.name()).display(new ProxyDisplayInfo.Display().id(id() + "_").type(type)));
            post("/personify", parameters());
        } catch (RestfulFailure | MalformedURLException error) {
            notifier.refreshError(errorMessage(unit.url()));
        }
    }

    protected void request(String operation) {
        request(operation, null);
    }

    protected void request(String operation, Object object) {
        try {

            if (!ready) {
                this.pendingRequestList.add(new PendingRequest().operation(operation).parameter(object));
                return;
            }

            Map<String, String> map = new HashMap<>();
            if (object != null) map.put("value", serializeParameter(object).toString());
            post("?operation=" + operation, map);
        } catch (RestfulFailure | MalformedURLException error) {
            notifier.refreshError(errorMessage(unit.url()));
        }
    }

    public void ready() {
        this.ready = true;
        processPendingRequests();
    }

    private static final Map<String, String> errorMessages = new HashMap<>() {{
        put("es", "No se pudo conectar con %s");
        put("en", "Could not connect with %s");
    }};
    protected String errorMessage(String application) {
        String language = language();
        if (!errorMessages.containsKey(language)) language = "en";
        return String.format(errorMessages.get(language), application);
    }

    private void post(String subPath, Map<String, String> parameters) throws MalformedURLException, RestfulFailure {
        URL unitUrl = new URL(this.unit.url());
        new RestAccessor().post(unitUrl, path + "/" + id() + subPath, withInternalParameters(parameters));
    }

    private JsonElement serializeParameter(Object value) {
        String result = Json.toString(value);
        try {
            return Parser.parse(result);
        } catch (Exception var4) {
            return new JsonPrimitive(result);
        }
    }

    private void processPendingRequests() {
        pendingRequestList.forEach(r -> request(r.operation, r.parameter));
        pendingRequestList.clear();
    }

    private Map<String, String> withInternalParameters(Map<String, String> parameters) {
        Map<String, String> result = new HashMap<>();
        parameters.forEach(result::put);
        result.put("client", clientId);
        result.put("session", sessionId);
        result.put("language", language());
        result.put("token", token);
        result.put("personifiedDisplay", id() + "_");
        return result;
    }

    private String language() {
        UIClient client = session().client();
        if (client != null) return client.language();
        return session().discoverLanguage();
    }

    private class PendingRequest {
        private String operation;
        private Object parameter;

        PendingRequest operation(String operation) {
            this.operation = operation;
            return this;
        }

        PendingRequest parameter(Object parameter) {
            this.parameter = parameter;
            return this;
        }
    }
}
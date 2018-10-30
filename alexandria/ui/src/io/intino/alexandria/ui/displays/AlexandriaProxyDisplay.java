package io.intino.alexandria.ui.displays;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.intino.alexandria.rest.spark.ResponseAdapter;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.restful.core.RestfulAccessor;
import io.intino.alexandria.restful.exceptions.RestfulFailure;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class AlexandriaProxyDisplay<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private final String sessionId;
    private final String clientId;
    private final String token;
    private final String appUrl;
    private final String path;
    private String personifiedDisplayId;
    private static final JsonParser Parser = new JsonParser();
    private Set<PendingRequest> pendingRequestList = new LinkedHashSet<>();

    public AlexandriaProxyDisplay(UISession session, String appUrl, String path) {
        this.sessionId = session.id();
        this.clientId = session.client().id();
        this.token = session.token().id();
        this.appUrl = appUrl;
        this.path = path;
    }

    @Override
    public void refresh() {
        try {
            if (personifiedDisplayId == null) return;
            post("?operation=refreshPersonifiedDisplay", parameters());
        } catch (RestfulFailure | MalformedURLException error) {
            refreshError(errorMessage("amidas"));
        }
    }

    protected abstract Map<String, String> parameters();
    protected abstract void refreshBaseUrl(String appUrl);
    protected abstract void refreshError(String error);

    @Override
    protected void init() {
        super.init();
        try {
            refreshBaseUrl(appUrl);
            post("/personify", parameters());
        } catch (RestfulFailure | MalformedURLException error) {
            refreshError(errorMessage("amidas"));
        }
    }

    protected void request(String operation) {
        request(operation, null);
    }

    protected void request(String operation, Object object) {
        try {

            if (personifiedDisplayId == null) {
                this.pendingRequestList.add(new PendingRequest().operation(operation).parameter(object));
                return;
            }

            Map<String, String> map = new HashMap<>();
            if (object != null) map.put("value", serializeParameter(object).toString());
            post("?operation=" + operation, map);
        } catch (RestfulFailure | MalformedURLException error) {
            refreshError(errorMessage("amidas"));
        }
    }

    public void registerPersonifiedDisplay(String id) {
        this.personifiedDisplayId = id;
        processPendingRequests();
    }

    protected String errorMessage(String application) {
        String language = session().browser().language();
        if (!errorMessages.containsKey(language)) language = "en";
        return String.format(errorMessages.get(language), application);
    }

    private void post(String subPath, Map<String, String> parameters) throws MalformedURLException, RestfulFailure {
        URL appUrl = new URL(this.appUrl);
        parameters.put("client", clientId);
        parameters.put("session", sessionId);
        parameters.put("token", token);
        parameters.put("personifiedDisplay", personifiedDisplayId);
        new RestfulAccessor().post(appUrl, path + "/" + id() + subPath, parameters);
    }

    private JsonElement serializeParameter(Object value) {
        String result = ResponseAdapter.adapt(value);

        try {
            return Parser.parse(result);
        } catch (Exception var4) {
            return new JsonPrimitive(result);
        }
    }

    private static Map<String, String> errorMessages = new HashMap<String, String>() {{
        put("es", "no se pudo conectar con %s");
        put("en", "could not connect with %s");
    }};

    private void processPendingRequests() {
        pendingRequestList.forEach(r -> request(r.operation, r.parameter));
        pendingRequestList.clear();
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
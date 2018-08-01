package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.services.push.UISession;
import io.intino.konos.restful.core.RestfulAccessor;
import io.intino.konos.restful.exceptions.RestfulFailure;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class AlexandriaDelegateDisplay<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private final String sessionId;
    private final String clientId;
    private final String token;
    private final String appUrl;
    private final String path;
    private String personifiedDisplayId;

    public AlexandriaDelegateDisplay(UISession session, String appUrl, String path) {
        this.sessionId = session.id();
        this.clientId = session.client().id();
        this.token = session.token().id();
        this.appUrl = appUrl;
        this.path = path;
    }

    private static Map<String, String> errorMessages = new HashMap<String, String>() {{
        put("es", "no se pudo conectar con %s");
        put("en", "could not connect with %s");
    }};

    protected abstract Map<String, String> parameters();
    protected abstract void refreshError(String error);

    @Override
    protected void init() {
        super.init();
        try {
            post("/personify", parameters());
        } catch (RestfulFailure | MalformedURLException error) {
            refreshError(errorMessage("amidas"));
        }
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

    public void registerPersonifiedDisplay(String id) {
        this.personifiedDisplayId = id;
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
}
package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.services.push.UISession;
import io.intino.konos.restful.core.RestfulAccessor;
import io.intino.konos.restful.exceptions.RestfulFailure;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class AlexandriaDelegateDisplay<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private final UISession session;
    private final String appUrl;
    private final String path;
    private String personifiedDisplayId;

    public AlexandriaDelegateDisplay(UISession session, String appUrl, String path) {
        this.session = session;
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
            post("/?operation=refreshPersonifiedDisplay", parameters());
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
        parameters.put("client", session.client().id());
        parameters.put("session", session.id());
        parameters.put("token", session.token().id());
        parameters.put("personifiedDisplay", personifiedDisplayId);
        new RestfulAccessor().post(appUrl, path + "/" + id() + subPath, parameters);
    }
}
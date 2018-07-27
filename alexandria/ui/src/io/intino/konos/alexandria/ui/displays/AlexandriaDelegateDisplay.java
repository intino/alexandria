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

    public AlexandriaDelegateDisplay(UISession session) {
        this.session = session;
    }

    private static Map<String, String> errorMessages = new HashMap<String, String>() {{
        put("es", "no se pudo conectar con %s");
        put("en", "could not connect with %s");
    }};

    protected void personifyElement(String appUrl, String path, Map<String, String> parameters) throws MalformedURLException, RestfulFailure {
        URL amidasUrl = new URL(appUrl);
        parameters.put("client", session.client().id());
        parameters.put("session", session.id());
        parameters.put("token", session.token().id());
        new RestfulAccessor().post(amidasUrl, path, parameters);
    }

    protected String errorMessage(String application) {
        String language = session().browser().language();
        if (!errorMessages.containsKey(language)) language = "en";
        return String.format(errorMessages.get(language), application);
    }
}
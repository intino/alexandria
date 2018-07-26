package io.intino.konos.alexandria.ui.displays;

import java.util.HashMap;
import java.util.Map;

public class AlexandriaDelegateDisplay<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private static Map<String, String> errorMessages = new HashMap<String, String>() {{
        put("es", "no se pudo conectar con %s");
        put("en", "could not connect with %s");
    }};

    protected String errorMessage(String language, String application) {
        if (!errorMessages.containsKey(language)) language = "en";
        return String.format(errorMessages.get(language), application);
    }
}
package io.intino.alexandria.ui.displays;

import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ActivityDisplay<DN extends AlexandriaDisplayNotifier, B extends Box> extends AlexandriaDisplay<DN> {
    protected B box;

    public ActivityDisplay(B box) {
        this.box = box;
    }

    public User user() {
        return session().user();
    }

    public URL baseAssetUrl() {
        try {
            return new URL(session().browser().baseAssetUrl());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String currentLanguage() {
        UISession session = session();
        if (session == null) return "en";
        UIClient client = session.client();
        return client != null ? client.language() : session().browser().languageFromMetadata();
    }

    public int timezoneOffset() {
        return session().browser().timezoneOffset();
    }

    public void timezoneOffset(int value) {
        session().browser().timezoneOffset(value/60);
    }

}


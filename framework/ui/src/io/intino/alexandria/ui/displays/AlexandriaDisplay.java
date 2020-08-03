package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AlexandriaDisplay<DN extends DisplayNotifier, B extends Box> extends Display<DN, B> {

    public AlexandriaDisplay(B box) {
        super(box);
    }

    public User user() {
        return session().user();
    }

    public String username() {
        UISession session = session();
        if (session == null) return null;
        return session.user() != null ? session.user().username() : null;
    }

    public URL baseAssetUrl() {
        try {
            return new URL(session().browser().baseAssetUrl());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String translate(String word) {
        return ((AlexandriaUiBox)box()).translatorService().translate(word, language());
    }

    public String language() {
        return session() != null ? session().discoverLanguage() : "en";
    }

    public int timezoneOffset() {
        return session().browser().timezoneOffset();
    }

    public void timezoneOffset(int value) {
        session().browser().timezoneOffset(value/60);
    }

}


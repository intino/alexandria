package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public abstract class ActivityDisplay<DN extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<DN> {
    protected Box box;

    public ActivityDisplay(Box box) {
        this.box = box;
    }

    public Optional<User> user() {
        return Optional.ofNullable(session().user());
    }

    public String username() {
        User user = session().user();
        return user != null ? user.username() : session().id();
    }

    public URL baseAssetUrl() {
        try {
            return new URL(session().browser().baseAssetUrl());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String currentLanguage() {
        ActivitySession session = session();
        if (session == null) return "en";
        ActivityClient client = (ActivityClient) session.currentClient();
        return client != null ? client.language() : session().browser().languageFromMetadata();
    }

    public int timezoneOffset() {
        return session().browser().timezoneOffset();
    }

    public void timezoneOffset(int value) {
        session().browser().timezoneOffset(value/60);
    }

}


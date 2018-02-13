package io.intino.konos.alexandria.activity.spark.actions;

import io.intino.konos.alexandria.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.User;

public class AuthenticateCallbackAction {
    public ActivitySession session;

    public void whenLoggedIn(User user) {
        updateSoulWith(user);
    }

    public void whenLoggedOut(User user) {
        updateSoulWith(null);
    }

    private void updateSoulWith(User user) {
        session.user(user);

        ActivityClient client = session.client();
        if (client == null) return;

        client.soul().user(user);
    }

}

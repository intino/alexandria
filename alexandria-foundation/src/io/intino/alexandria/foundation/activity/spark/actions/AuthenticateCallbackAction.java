package io.intino.alexandria.foundation.activity.spark.actions;

import io.intino.alexandria.foundation.activity.services.push.ActivityClient;
import io.intino.alexandria.foundation.activity.services.push.ActivitySession;
import io.intino.alexandria.foundation.activity.services.push.User;

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

        ActivityClient client = (ActivityClient) session.currentClient();
        if (client == null) return;

        client.soul().user(user);
    }

}

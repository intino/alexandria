package io.intino.pandora.server.activity.spark.actions;

import io.intino.pandora.server.activity.services.push.ActivityClient;
import io.intino.pandora.server.activity.services.push.ActivitySession;
import io.intino.pandora.server.activity.services.push.User;

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

        client.soul().applicationDisplay().user(user);
    }

}

package io.intino.konos.alexandria.activity.spark.actions;

import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.User;

public class AuthenticateCallbackAction {
    public ActivitySession session;

    public void whenLoggedIn(User user) {
        updateUser(user);
    }

    public void whenLoggedOut(User user) {
        updateUser(null);
    }

    private void updateUser(User user) {
        session.user(user);
    }

}

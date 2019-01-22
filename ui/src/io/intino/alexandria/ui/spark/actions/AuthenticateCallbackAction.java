package io.intino.alexandria.ui.spark.actions;

import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;

public class AuthenticateCallbackAction {
    public UISession session;

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

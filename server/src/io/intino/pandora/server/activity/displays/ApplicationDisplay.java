package io.intino.pandora.server.activity.displays;

import io.intino.pandora.server.activity.displays.notifiers.ApplicationDisplayNotifier;
import io.intino.pandora.server.activity.pushservice.User;

public abstract class ApplicationDisplay extends Display<ApplicationDisplayNotifier> {
    public abstract void user(User user);
}

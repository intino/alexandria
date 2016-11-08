package io.intino.pandora.server.activity.displays.notifiers;

import io.intino.pandora.server.activity.displays.Display;
import io.intino.pandora.server.activity.displays.DisplayNotifier;
import io.intino.pandora.server.activity.displays.MessageCarrier;
import io.intino.pandora.server.activity.services.push.User;

public class ApplicationDisplayNotifier extends DisplayNotifier {

    public ApplicationDisplayNotifier(Display display, MessageCarrier carrier) {
        super(display, carrier);
    }

    public void userRefresh(User user) {
        putToOwner("userRefresh", "user", user);
    }

    public void userLoggedOut() {
        this.putToOwner("userLoggedOut");
    }

}

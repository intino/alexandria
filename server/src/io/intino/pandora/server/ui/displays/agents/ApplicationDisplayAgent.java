package io.intino.pandora.server.ui.displays.agents;

import io.intino.pandora.server.ui.displays.Display;
import io.intino.pandora.server.ui.displays.DisplayAgent;
import io.intino.pandora.server.ui.displays.MessageCarrier;
import io.intino.pandora.server.ui.pushservice.User;

public class ApplicationDisplayAgent extends DisplayAgent {

    public ApplicationDisplayAgent(Display display, MessageCarrier carrier) {
        super(display, carrier);
    }

    public void userRefresh(User user) {
        putToOwner("userRefresh", "user", user);
    }

    public void userLoggedOut() {
        this.putToOwner("userLoggedOut");
    }

}

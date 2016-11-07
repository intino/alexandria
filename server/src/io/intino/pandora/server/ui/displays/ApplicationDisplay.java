package io.intino.pandora.server.ui.displays;

import io.intino.pandora.server.ui.displays.agents.ApplicationDisplayAgent;
import io.intino.pandora.server.ui.pushservice.User;

public abstract class ApplicationDisplay extends Display<ApplicationDisplayAgent> {
    public abstract void user(User user);
}

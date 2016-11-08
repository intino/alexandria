package io.intino.pandora.server.activity.services.auth;

import java.net.URL;

public interface Space {
    String name();
    String title();
    String secret();
    URL url();
    URL logo();
    URL authenticateCallbackUrl();
}

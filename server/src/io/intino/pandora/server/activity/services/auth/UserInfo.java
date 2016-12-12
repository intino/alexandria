package io.intino.pandora.server.activity.services.auth;

import java.net.URL;

public interface UserInfo {
    String username();
    String fullName();
    URL photo();
    String email();
    String language();
}
